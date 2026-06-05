package com.ai.ops.repository;

import com.ai.ops.entity.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<AlertEntity, Long> {
}
