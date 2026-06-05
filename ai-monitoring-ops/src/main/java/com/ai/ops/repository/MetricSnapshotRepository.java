package com.ai.ops.repository;

import com.ai.ops.entity.MetricSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricSnapshotRepository
        extends JpaRepository<MetricSnapshotEntity, Long> {
}