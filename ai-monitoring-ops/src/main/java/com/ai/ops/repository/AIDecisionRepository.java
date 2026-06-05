package com.ai.ops.repository;

import com.ai.ops.entity.AIDecisionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIDecisionRepository
        extends JpaRepository<AIDecisionEntity, Long> {
}