package com.ai.ops.repository;

import com.ai.ops.entity.ScalingHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
@Repository
public interface ScalingHistoryRepository extends JpaRepository<ScalingHistoryEntity, Long> {

    List<ScalingHistoryEntity> findTop10ByOrderByCreatedAtDesc();
}