package com.ai.ops.controller;

import com.ai.ops.entity.AIDecisionEntity;
import com.ai.ops.model.*;
import com.ai.ops.service.*;
import com.ai.ops.util.RootCauseParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ai")
public class AIDecisionController {

    private final AIDecisionAuditService aiDecisionAuditService;
    private final RootCauseService rootCauseService;
    private final ForecastService forecastService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final ExecutiveSummaryService executiveSummaryService;

    @GetMapping("/last-decision")
    public AIRecommendation recommendation() {

        AIDecisionEntity decision = aiDecisionAuditService.getLastDecision();

        if (decision == null) {
            return AIRecommendation.builder().action("NO_ACTION").
                    reason("No decisions available").confidence(0).build();
        }

        return AIRecommendation.builder().action(decision.getAction()).
                replicas(decision.getReplicas()).reason(decision.getReason()).
                confidence(decision.getConfidence()).build();
    }

    @GetMapping("/root-cause")
    public RootCauseResponse getRootCause() {
        String response = rootCauseService.analyze();
        log.info("AI Root Cause Analysis Response: {}", response);
        return RootCauseParser.parse(response);
    }

    @GetMapping("/forecast")
    public ForecastResponse forecast() {
        return forecastService.forecast();
    }

    @GetMapping("/anomalies")
    public List<AnomalyDto> anomalies() {
        return anomalyDetectionService.detect();
    }
    @GetMapping("/executive-summary")
    public ExecutiveSummaryResponse summary() {
        return executiveSummaryService.generate();
    }
}