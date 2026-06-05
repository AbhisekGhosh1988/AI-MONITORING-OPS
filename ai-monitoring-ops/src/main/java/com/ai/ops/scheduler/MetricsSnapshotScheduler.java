package com.ai.ops.scheduler;

import com.ai.ops.model.PodStatusDto;
import com.ai.ops.service.MetricsHistoryService;
import com.ai.ops.service.PrometheusService;
import io.kubernetes.client.openapi.models.V1Pod;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricsSnapshotScheduler {

    private final PrometheusService prometheusService;
    private final MetricsHistoryService historyService;

    @Scheduled(fixedRate = 60000)
    public void collectMetrics() {

        try {
            List<PodStatusDto> pods = prometheusService.getPodStatuses();
            int runningPods = (int) pods.stream().
                    filter(p -> p.getPodName().contains("order-service")).
                    filter(p -> "Running".equals(p.getPhase())).count();

            historyService.saveSnapshot(prometheusService.getOrderServiceCpu(),
                    prometheusService.getOrderServiceMemoryMb(), runningPods,
                    prometheusService.getRestartCount());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}