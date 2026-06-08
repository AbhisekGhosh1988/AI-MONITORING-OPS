package com.ai.ops.service;

import com.ai.ops.entity.AIDecisionEntity;
import com.ai.ops.repository.AIDecisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AIDecisionAuditService {

    private final AIDecisionRepository repository;

    public void saveDecision(String action, Integer replicas, String reason,
                             Integer confidence, String aiExplanation, boolean executed) {

        AIDecisionEntity entity = AIDecisionEntity.builder().action(action).
                replicas(replicas).reason(reason).confidence(confidence).
                aiExplanation(aiExplanation).executed(executed).
                createdAt(LocalDateTime.now()).build();
        repository.save(entity);
    }

    public List<AIDecisionEntity> getAll() {
        return repository.findAll();
    }

    public AIDecisionEntity getLastDecision() {
        List<AIDecisionEntity> decisions = repository.findAll();

        if (decisions.isEmpty()) {
            return null;
        }
        return decisions.get(decisions.size() - 1);
    }
}