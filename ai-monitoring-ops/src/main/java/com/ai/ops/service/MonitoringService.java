package com.ai.ops.service;

import com.ai.ops.model.AIDecision;
import com.ai.ops.model.ClusterMetrics;
import com.ai.ops.model.LastDecisionResponse;
import com.ai.ops.util.AiResponseParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MonitoringService {

    private final AlertService alertService;
    private final AutoScalingService autoScalingService;
    private final AIDecisionService aiDecisionService;
    private final ObjectMapper objectMapper;
    private final PrometheusService prometheusService;
    private final AIDecisionAuditService aiAuditService;
    private Double lastCpuPercent;
    private Double lastMemoryMb;
    private Integer lastRestartCount;
    private Integer lastRunningPods;

    private LocalDateTime lastAiExecutionTime;

    private static final int AI_COOLDOWN_MINUTES = 5;
    public void monitorCluster() {

        try {

            long runningPods = (long) prometheusService.
                    query("count(kube_pod_status_phase{" + "phase=\"Running\"," + "pod=~\"order-service.*\"" + "})");

            long failedPods = (long) prometheusService.
                    query("count(\n" +
                            "  kube_pod_status_phase{\n" +
                            "    phase=\"Failed\",\n" +
                            "    pod=~\"order-service.*\"\n" +
                            "  } == 1\n" +
                            ")");

            int restartCount = (int) prometheusService.
                    query("sum(kube_pod_container_status_restarts_total{" + "pod=~\"order-service.*\"" + "})");

            double cpuPercent = prometheusService.
                    query("sum(rate(container_cpu_usage_seconds_total{" + "pod=~\"order-service.*\"" + "}[5m])) * 100");
            double memoryBytes = prometheusService.
                            query("sum(container_memory_working_set_bytes{" + "pod=~\"order-service.*\"" + "})");

            double memoryMb = memoryBytes / (1024 * 1024);
            ClusterMetrics metrics = ClusterMetrics.builder().runningPods((int) runningPods).
                    failedPods((int) failedPods).cpuPercent(cpuPercent).memoryMb(memoryMb).
                    restartCount(restartCount).build();
            String metricsJson = objectMapper.writeValueAsString(metrics);
            log.info("Collected Metrics: {}", metricsJson);
            if (!shouldInvokeAI(cpuPercent, memoryMb, restartCount, (int) runningPods, (int) failedPods)) {
                log.info("Cluster healthy. Skipping AI analysis.");
                updatePreviousMetrics(cpuPercent, memoryMb, restartCount, (int) runningPods);
                return;
            }

            if (aiCooldownActive()) {
                log.info("AI cooldown active. Skipping AI call.");
                updatePreviousMetrics(cpuPercent, memoryMb, restartCount, (int) runningPods);
                return;
            }
            log.info("AI analysis triggered");
            String aiResponse = aiDecisionService.analyze(metricsJson);
            lastAiExecutionTime = LocalDateTime.now();
            log.info("AI Response: {}", aiResponse);
            AIDecision decision = AiResponseParser.parse(aiResponse);
            log.info("Parsed AI Decision: {}", objectMapper.writeValueAsString(decision));
            aiAuditService.saveDecision(decision.getAction(), decision.getReplicas(),
                    decision.getReason(), decision.getConfidence(), false);

            executeDecision(decision);
            updatePreviousMetrics(cpuPercent, memoryMb, restartCount, (int) runningPods);

        } catch (Exception e) {
            log.error("Monitoring failed", e);
            alertService.createAlert("ERROR", e.getMessage());
        }
    }

    private void executeDecision(AIDecision decision) {

        try {
            String action = decision.getAction();
            if (action == null) {
                alertService.createAlert("ERROR", "AI returned null action");
                return;
            }

            switch (action) {
                case "SCALE_UP" -> {
                    if (!autoScalingService.canScale()) {
                        log.info("Cooldown active. Skipping scale up.");
                        return;
                    }

                    int replicas = decision.getReplicas() != null ? decision.getReplicas() : 3;

                    boolean scaled = autoScalingService.scale("order-service",
                            replicas);

                    if (scaled) {
                        aiAuditService.saveDecision(decision.getAction(), replicas,
                                decision.getReason(), decision.getConfidence(), true);
                        autoScalingService.addScalingEvent("SCALE_UP", replicas,
                                decision.getReason());
                        alertService.createAlert("HIGH", decision.getReason());
                    }
                }

                case "SCALE_DOWN" -> {
                    if (!autoScalingService.canScale()) {
                        log.info("Cooldown active. Skipping scale down.");
                        return;
                    }

                    int replicas = decision.getReplicas() != null ? decision.getReplicas() : 1;

                    boolean scaled = autoScalingService.scale("order-service",
                            replicas);

                    if (scaled) {
                        aiAuditService.saveDecision(decision.getAction(), replicas,
                                decision.getReason(), decision.getConfidence(), true);
                        autoScalingService.addScalingEvent("SCALE_DOWN", replicas, decision.getReason());
                        alertService.createAlert("MEDIUM", decision.getReason());
                    }
                }
                case "ALERT" -> alertService.createAlert("MEDIUM", decision.getReason());

                case "RESTART_POD" -> {
                    alertService.createAlert("CRITICAL", decision.getReason());
                    log.warn("Pod restart requested by AI. " + "Implement restart logic if needed.");
                }

                case "NO_ACTION" -> log.info("AI decided no action required.");

                default -> alertService.createAlert("ERROR", "Unknown AI action: " + action);
            }

        } catch (Exception e) {
            log.error("Decision execution failed", e);
            alertService.createAlert("ERROR", e.getMessage());
        }
    }
    private boolean shouldInvokeAI(double cpuPercent, double memoryMb, int restartCount, int runningPods, int failedPods) {

        // Critical conditions
        if (failedPods > 0) {return true;}
        if (restartCount > 0 && (lastRestartCount == null || restartCount > lastRestartCount)) {return true;}
        if (cpuPercent > 70) {return true;}
        if (memoryMb > 1024) {return true;}
        if (runningPods < 2) {return true;}
        // First run
        if (lastCpuPercent == null) {return true;}

        boolean cpuChanged = Math.abs(cpuPercent - lastCpuPercent) > 20;

        boolean memoryChanged = Math.abs(memoryMb - lastMemoryMb) > 500;

        boolean podCountChanged = !runningPodsEquals(runningPods);

        return cpuChanged || memoryChanged || podCountChanged;
    }

    private boolean runningPodsEquals(int runningPods) {
        return lastRunningPods != null && lastRunningPods == runningPods;
    }

    private boolean aiCooldownActive() {
        if (lastAiExecutionTime == null) {
            return false;
        }
        return lastAiExecutionTime.plusMinutes(AI_COOLDOWN_MINUTES).isAfter(LocalDateTime.now());
    }

    private void updatePreviousMetrics(double cpuPercent, double memoryMb, int restartCount, int runningPods) {

        this.lastCpuPercent = cpuPercent;
        this.lastMemoryMb = memoryMb;
        this.lastRestartCount = restartCount;
        this.lastRunningPods = runningPods;
    }
}