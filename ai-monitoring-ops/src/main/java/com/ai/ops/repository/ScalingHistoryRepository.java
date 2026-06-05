package com.ai.ops.repository;

import com.ai.ops.entity.ScalingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScalingHistoryRepository extends JpaRepository<ScalingHistoryEntity, Long> {
}