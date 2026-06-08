package com.ai.ops.model;

import lombok.Data;

import java.util.List;

@Data
public class ScalingDecisionResponse {

    private String action;

    private int confidence;

    private List<String> reasons;
}