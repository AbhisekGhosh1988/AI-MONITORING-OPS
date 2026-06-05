package com.ai.ops.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIDecision {

    private String action;

    private Integer replicas;

    private String reason;

    private Double confidence;
}