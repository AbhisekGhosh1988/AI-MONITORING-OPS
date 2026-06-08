package com.ai.ops.service;

import com.ai.ops.entity.ScalingHistoryEntity;
import com.ai.ops.model.ClusterContext;
import com.ai.ops.repository.ScalingHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ClusterContextService {

    private final PrometheusService prometheusService;
    private final ScalingHistoryRepository scalingHistoryRepository;
    private final AlertService alertService;

    public ClusterContext buildContext() {

        List<String> scalingEvents = scalingHistoryRepository.
                findTop10ByOrderByCreatedAtDesc().stream().map(this::formatScalingEvent).
                toList();

        return ClusterContext.builder()
                .cpu(prometheusService.getOrderServiceCpu())
                .memory(prometheusService.getOrderServiceMemoryMb())
                .runningPods(prometheusService.getRunningPods())
                .restartCount(prometheusService.getPodRestartCount())
                .alerts(alertService.getAlerts().size())
                .recentScalingEvents(scalingEvents)
                .build();
    }

    private String formatScalingEvent(ScalingHistoryEntity history) {
        return String.format("%s to %d replicas (%s)", history.getAction(),
                history.getReplicas(), history.getCreatedAt());
    }
}