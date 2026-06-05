package com.ai.ops.controller;

import com.ai.ops.model.DashboardFullResponse;
import com.ai.ops.model.DashboardSummary;
import com.ai.ops.model.PodStatusDto;
import com.ai.ops.service.*;
import io.kubernetes.client.openapi.models.V1Pod;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@RequiredArgsConstructor
@RestController
        @RequestMapping ("/dashboard")
public class DashBoardController {

    private final AlertService alertService;
    private final AutoScalingService autoScalingService;
    private final PrometheusService prometheusService;
    private final MetricsHistoryService metricsHistoryService;
    private final AIDecisionAuditService aiDecisionAuditService;

    @GetMapping("/scaling-history")
    public Map<String, Object> dashboard() {
        return Map.of("alerts", alertService.getAlerts(),
                "scalingHistory", autoScalingService.getHistory());
    }
    @GetMapping("/summary")
    public DashboardSummary summary() throws Exception {
        List<PodStatusDto> pods = prometheusService.getPodStatuses();
        int runningPods = (int) pods.stream().
                filter(p -> p.getPodName().contains("order-service")).
                filter(p -> "Running".equals(p.getPhase())).count();

        String lastAction = "NONE";

        return DashboardSummary.builder().runningPods(runningPods).
                cpuPercent(prometheusService.getOrderServiceCpu()).
                memoryMb(prometheusService.getOrderServiceMemoryMb()).
                restartCount(prometheusService.getRestartCount()).
                alerts(alertService.getAlerts().size()).lastAiAction(lastAction).build();
    }


    @GetMapping("/metrics/history")
    public Object history() {
        return metricsHistoryService.getHistory();
    }
    @GetMapping("/metrics/history/latest")
    public Object historyLatest() {
        return metricsHistoryService.getLatest100();
    }
    @GetMapping("/details")
    public DashboardFullResponse dashboardFull() throws Exception {

        List<PodStatusDto> pods = prometheusService.getPodStatuses();
        int runningPods = (int) pods.stream().
                filter(p -> p.getPodName().contains("order-service")).
                filter(p -> "Running".equals(p.getPhase())).count();

        String lastAction = "NONE";

        DashboardSummary summary =
                DashboardSummary.builder().runningPods(runningPods).
                        cpuPercent(prometheusService.getOrderServiceCpu()).
                        memoryMb(prometheusService.getOrderServiceMemoryMb()).
                        restartCount(prometheusService.getRestartCount()).
                        alerts(alertService.getAlerts().size()).
                        lastAiAction(lastAction).build();

        return DashboardFullResponse.builder().summary(summary).
                alerts(alertService.getAlerts()).
                scalingHistory(autoScalingService.getHistory()).
                aiDecisions(aiDecisionAuditService.getAll()).
                metricsHistory(metricsHistoryService.getLatest100()).build();
    }
}


