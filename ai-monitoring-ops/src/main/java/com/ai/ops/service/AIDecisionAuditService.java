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
                             Double confidence, boolean executed){

        repository.save(AIDecisionEntity.builder().createdAt(LocalDateTime.now()).
                action(action).replicas(replicas).reason(reason).executed(executed).
                confidence(confidence).build());
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