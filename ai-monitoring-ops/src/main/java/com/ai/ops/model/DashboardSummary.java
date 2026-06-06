package com.ai.ops.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class DashboardSummary {

    private Double cpuPercent;
    private Double memoryMb;
    private Integer alerts;
    private String lastAiAction;
    private Map<String, Integer> podDetails;
}
