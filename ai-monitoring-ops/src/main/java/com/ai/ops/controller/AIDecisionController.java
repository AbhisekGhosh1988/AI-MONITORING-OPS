package com.ai.ops.controller;

import com.ai.ops.entity.AIDecisionEntity;
import com.ai.ops.model.AIRecommendation;
import com.ai.ops.service.AIDecisionAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AIDecisionController {

    private final AIDecisionAuditService aiDecisionAuditService;

    @GetMapping("/ai/recommendation")
    public AIRecommendation recommendation() {

        AIDecisionEntity decision = aiDecisionAuditService.getLastDecision();

        if (decision == null) {
            return AIRecommendation.builder().action("NO_ACTION").
                    reason("No decisions available").confidence(0.0).build();
        }

        return AIRecommendation.builder().action(decision.getAction()).
                replicas(decision.getReplicas()).reason(decision.getReason()).
                confidence(decision.getConfidence()).build();
    }
    @GetMapping("/ai/decisions")
    public Object decisions() {
        return aiDecisionAuditService.getAll();
    }
}