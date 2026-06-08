package com.ai.ops.service;

import com.ai.ops.entity.MetricSnapshotEntity;
import com.ai.ops.model.ForecastResponse;
import com.ai.ops.repository.MetricSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final MetricSnapshotRepository repository;

    public ForecastResponse forecast() {

        List<MetricSnapshotEntity> metrics =
                repository.findTop20ByOrderByCreatedAtDesc();

        if (metrics.size() < 5) {

            MetricSnapshotEntity latest =
                    metrics.get(0);

            return ForecastResponse.builder()
                    .currentCpu(latest.getCpuPercent())
                    .cpu15Min(latest.getCpuPercent())
                    .cpu30Min(latest.getCpuPercent())
                    .currentMemory(latest.getMemoryMb())
                    .memory15Min(latest.getMemoryMb())
                    .memory30Min(latest.getMemoryMb())
                    .build();
        }

        MetricSnapshotEntity latest =
                metrics.get(0);
        if (metrics.isEmpty()) {

            return ForecastResponse.builder()
                    .currentCpu(0.0)
                    .cpu15Min(0.0)
                    .cpu30Min(0.0)
                    .currentMemory(0.0)
                    .memory15Min(0.0)
                    .memory30Min(0.0)
                    .build();
        }

        double avgCpu =
                metrics.stream()
                        .mapToDouble(MetricSnapshotEntity::getCpuPercent)
                        .average()
                        .orElse(latest.getCpuPercent());

        double avgMemory =
                metrics.stream()
                        .mapToDouble(MetricSnapshotEntity::getMemoryMb)
                        .average()
                        .orElse(latest.getMemoryMb());

        return ForecastResponse.builder()
                .currentCpu(latest.getCpuPercent())
                .cpu15Min(round(avgCpu))
                .cpu30Min(round(avgCpu * 1.10))
                .currentMemory(latest.getMemoryMb())
                .memory15Min(round(avgMemory))
                .memory30Min(round(avgMemory * 1.10))
                .build();
    }

    private double round(double value) {

        return Math.round(value * 100.0) / 100.0;
    }
}