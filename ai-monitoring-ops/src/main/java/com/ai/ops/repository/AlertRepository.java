package com.ai.ops.repository;

import com.ai.ops.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
    List<AlertEntity> findByCreatedAtAfter(LocalDateTime time);
}
