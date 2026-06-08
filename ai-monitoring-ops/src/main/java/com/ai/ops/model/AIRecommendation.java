package com.ai.ops.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AIRecommendation {

    private String action;

    private Integer replicas;

    private String reason;

    private Integer confidence;
}