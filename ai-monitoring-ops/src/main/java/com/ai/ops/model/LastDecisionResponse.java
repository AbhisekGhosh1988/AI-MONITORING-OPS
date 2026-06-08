package com.ai.ops.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class LastDecisionResponse {

    private String action;

    private Integer replicas;

    private String reason;

    private Double confidence;

    private List<String> reasons;
}