package com.ai.ops.service;

import com.ai.ops.model.AIDecision;
import com.ai.ops.model.ClusterMetrics;
import com.ai.ops.model.LastDecisionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public void monitorCluster() {

        try {

            long runningPods = (long) prometheusService.
                    query("count(kube_pod_status_phase{" + "phase=\"Running\"," + "pod=~\"order-service.*\"" + "})");

            long failedPods = (long) prometheusService.
                    query("count(kube_pod_status_phase{" + "phase=\"Failed\"," + "pod=~\"order-service.*\"" + "})");

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
            String aiResponse = aiDecisionService.analyze(metricsJson);
            log.info("AI Response: {}", aiResponse);
            AIDecision decision = objectMapper.readValue(aiResponse, AIDecision.class);

            aiAuditService.saveDecision(decision.getAction(), decision.getReplicas(),
                    decision.getReason(), decision.getConfidence(), false);
            executeDecision(decision);

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
}