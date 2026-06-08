package com.ai.ops.repository;

import com.ai.ops.entity.MetricSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetricSnapshotRepository
        extends JpaRepository<MetricSnapshotEntity, Long> {

    List<MetricSnapshotEntity> findTop20ByOrderByCreatedAtDesc();

    List<MetricSnapshotEntity> findTop10ByOrderByCreatedAtDesc();
}