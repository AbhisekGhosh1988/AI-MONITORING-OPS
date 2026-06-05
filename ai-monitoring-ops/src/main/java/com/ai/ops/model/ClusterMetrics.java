package com.ai.ops.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClusterMetrics {

    private Integer runningPods;

    private Integer failedPods;

    private Double cpuPercent;

    private Double memoryMb;

    private Integer restartCount;
}
