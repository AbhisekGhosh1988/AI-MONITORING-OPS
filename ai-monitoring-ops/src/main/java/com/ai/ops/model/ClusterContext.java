package com.ai.ops.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ClusterContext {

    private double cpu;

    private double memory;

    private long runningPods;

    private long restartCount;

    private long alerts;

    private List<String> recentScalingEvents;
}