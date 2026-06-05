package com.ai.ops.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardSummary {

    private Integer runningPods;

    private Double cpuPercent;

    private Double memoryMb;

    private Integer restartCount;

    private Integer alerts;

    private String lastAiAction;
}
