package com.ai.ops.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastResponse {

    private Double currentCpu;

    private Double cpu15Min;

    private Double cpu30Min;

    private Double currentMemory;

    private Double memory15Min;

    private Double memory30Min;
}