package com.ai.ops.model;

import lombok.Data;

@Data
public class TestMetricsRequest {

    private Integer runningPods;

    private Integer failedPods;

    private Double cpuPercent;

    private Double memoryMb;

    private Integer restartCount;
}