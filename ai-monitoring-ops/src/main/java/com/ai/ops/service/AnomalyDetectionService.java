package com.ai.ops.service;

import com.ai.ops.entity.MetricSnapshotEntity;
import com.ai.ops.model.AnomalyDto;
import com.ai.ops.repository.MetricSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnomalyDetectionService {

    private final MetricSnapshotRepository repository;

    public List<AnomalyDto> detect() {

        List<MetricSnapshotEntity> metrics =
                repository.findTop10ByOrderByCreatedAtDesc();

        List<AnomalyDto> anomalies =
                new ArrayList<>();

        if (metrics.size() < 5) {
            return anomalies;
        }

        MetricSnapshotEntity latest =
                metrics.get(0);

        double avgCpu =
                metrics.stream()
                        .skip(1)
                        .mapToDouble(
                                MetricSnapshotEntity::getCpuPercent)
                        .average()
                        .orElse(0);

        double avgMemory =
                metrics.stream()
                        .skip(1)
                        .mapToDouble(
                                MetricSnapshotEntity::getMemoryMb)
                        .average()
                        .orElse(0);

        double avgRestarts =
                metrics.stream()
                        .skip(1)
                        .mapToInt(
                                MetricSnapshotEntity::getRestartCount)
                        .average()
                        .orElse(0);

        if (latest.getCpuPercent() >
                avgCpu * 1.1) {

            anomalies.add(
                    AnomalyDto.builder()
                            .type("CPU")
                            .severity("HIGH")
                            .message(
                                    "CPU usage increased unexpectedly")
                            .build()
            );
        }

        if (latest.getMemoryMb() >
                avgMemory * 1.5) {

            anomalies.add(
                    AnomalyDto.builder()
                            .type("MEMORY")
                            .severity("HIGH")
                            .message(
                                    "Memory consumption spike detected")
                            .build()
            );
        }

        if (latest.getRestartCount() >
                avgRestarts + 3) {

            anomalies.add(
                    AnomalyDto.builder()
                            .type("RESTART")
                            .severity("CRITICAL")
                            .message(
                                    "Pod restart anomaly detected")
                            .build()
            );
        }

        if (latest.getRunningPods() <
                metrics.get(1).getRunningPods()) {

            anomalies.add(
                    AnomalyDto.builder()
                            .type("PODS")
                            .severity("MEDIUM")
                            .message(
                                    "Running pod count decreased")
                            .build()
            );
        }

        return anomalies;
    }
}