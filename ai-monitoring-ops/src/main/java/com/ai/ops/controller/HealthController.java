package com.ai.ops.controller;

import com.ai.ops.model.HealthResponse;
import com.ai.ops.model.HealthScoreResponse;
import com.ai.ops.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private final HealthService healthService;

    @GetMapping
    public HealthResponse health() {
        return healthService.getHealth();
    }
    @GetMapping("/health-score")
    public HealthScoreResponse getHealthScore() {
        return healthService.calculate();
    }
}