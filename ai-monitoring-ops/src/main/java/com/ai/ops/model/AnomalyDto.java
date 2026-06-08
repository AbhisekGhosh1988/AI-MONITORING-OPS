package com.ai.ops.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnomalyDto {

    private String type;

    private String severity;

    private String message;
}