package com.ai.ops.service;

import com.ai.ops.entity.MetricSnapshotEntity;
import com.ai.ops.repository.MetricSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsHistoryService {

    private final MetricSnapshotRepository repository;

    public void saveSnapshot(Double cpuPercent, Double memoryMb, Integer runningPods,
                             Integer restartCount) {

        repository.save(MetricSnapshotEntity.builder().createdAt(LocalDateTime.now()).
                cpuPercent(cpuPercent).memoryMb(memoryMb).runningPods(runningPods).
                restartCount(restartCount).build());
    }

    public List<MetricSnapshotEntity> getHistory() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "createdAt"));
    }

    public List<MetricSnapshotEntity> getLatest100() {
        return repository.findAll(PageRequest.of(0, 100,
                Sort.by(Sort.Direction.DESC, "createdAt"))).getContent();
    }
}