package com.ai.ops.scheduler;

import com.ai.ops.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MonitoringScheduler {

    private final MonitoringService service;

    @Scheduled(fixedDelay = 30000)
    public void run() {
        log.info("Starting cluster monitoring...");
        service.monitorCluster();
        log.info("Cluster monitoring completed.");
    }
}