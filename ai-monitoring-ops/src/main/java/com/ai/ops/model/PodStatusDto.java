package com.ai.ops.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PodStatusDto {

    private String podName;
    private String phase;
    private String namespace;
    private double value;
}
