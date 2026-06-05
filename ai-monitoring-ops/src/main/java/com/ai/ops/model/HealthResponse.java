package com.ai.ops.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HealthResponse {

    private String application;

    private String database;

    private String kubernetes;

    private String prometheus;

    private String ai;
}