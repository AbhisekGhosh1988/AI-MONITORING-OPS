package com.ai.ops.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveSummaryResponse {

    private String summary;

    private String riskLevel;

    private Integer healthScore;
}