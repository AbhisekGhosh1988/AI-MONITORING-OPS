package com.ai.ops.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LastDecisionResponse {

    private LocalDateTime timestamp;

    private String action;

    private Integer replicas;

    private String reason;
}