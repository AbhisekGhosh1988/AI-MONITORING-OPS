package com.ai.ops.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MetricSnapshot {

    private LocalDateTime timestamp;

    private Double cpuPercent;

    private Double memoryMb;

    private Integer runningPods;

    private Integer restartCount;
}