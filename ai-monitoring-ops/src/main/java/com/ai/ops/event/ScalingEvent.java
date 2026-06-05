package com.ai.ops.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScalingEvent {

    private LocalDateTime timestamp;
    private String action;
    private Integer replicas;
    private String reason;
}
