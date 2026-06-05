package com.ai.ops.service;

import com.ai.ops.entity.ScalingHistoryEntity;
import com.ai.ops.repository.ScalingHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AutoScalingService {

    private final ScalingHistoryRepository repository;
    private static final String KUBECTL = "D:\\K8S\\kubectl.exe";
    private LocalDateTime lastScalingTime;
    private static final long COOLDOWN_MINUTES = 5;

    public boolean canScale() {
        if (lastScalingTime == null) {
            return true;
        }
        return lastScalingTime.plusMinutes(COOLDOWN_MINUTES).isBefore(LocalDateTime.now());
    }

    public int getCurrentReplicas(String deployment) throws Exception {
        Process process = new ProcessBuilder(KUBECTL, "get", "deployment",
                deployment, "-n", "default", "-o", "jsonpath={.spec.replicas}").start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String error = new String(process.getErrorStream().readAllBytes(),
                    StandardCharsets.UTF_8);
            throw new RuntimeException("Failed to get replicas: " + error);
        }

        String replicas = new String(process.getInputStream().readAllBytes(),
                StandardCharsets.UTF_8).trim();

        return Integer.parseInt(replicas);
    }

    public boolean scale(String deployment, int replicas) throws Exception {

        int currentReplicas = getCurrentReplicas(deployment);
        if (currentReplicas == replicas) {
            log.info("Skipping scale. Deployment {} already has {} replicas",
                    deployment, replicas);
            return false;
        }

        Process process = new ProcessBuilder(KUBECTL, "scale", "deployment",
                deployment, "--replicas=" + replicas, "-n", "default").start();

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            String error = new String(process.getErrorStream().readAllBytes(),
                    StandardCharsets.UTF_8);

            log.error("Scaling failed for deployment {} : {}", deployment, error);
            String output = new String(process.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);

            log.info("kubectl output = {}", output);
            log.error("kubectl error = {}", error);
            return false;
        }
        lastScalingTime = LocalDateTime.now();
        log.info("Scaled deployment {} from {} to {} replicas", deployment,
                currentReplicas, replicas);
        return true;
    }

    public void addScalingEvent(String action, Integer replicas, String reason) {

        repository.save(ScalingHistoryEntity.builder().
                createdAt(LocalDateTime.now()).action(action).replicas(replicas).
                reason(reason).build());
    }

    public List<ScalingHistoryEntity> getHistory() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}