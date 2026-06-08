package com.ai.ops.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RootCauseResponse {

    private String cause;

    private Integer confidence;

    private String recommendation;
}