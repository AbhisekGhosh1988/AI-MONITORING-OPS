package com.ai.ops.controller;

import com.ai.ops.model.ClusterMetrics;
import com.ai.ops.model.TestMetricsRequest;
import com.ai.ops.service.AIDecisionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final AIDecisionService aiDecisionService;
    @Autowired
    private ObjectMapper objectMapper;
    @PostMapping("/metrics")
    public String simulate(@RequestBody TestMetricsRequest request) throws Exception {

        ClusterMetrics metrics = ClusterMetrics.builder().
                runningPods(request.getRunningPods()).failedPods(request.getFailedPods()).
                cpuPercent(request.getCpuPercent()).memoryMb(request.getMemoryMb()).
                restartCount(request.getRestartCount()).build();
        log.info("AI response--------------->"
                +aiDecisionService.analyze(objectMapper.writeValueAsString(metrics)));
        return "Simulation completed";
    }
    @GetMapping("/test-kubectl")
    public String test() throws Exception {

        Process process = new ProcessBuilder
                ("D:\\K8S\\kubectl.exe", "get", "pods", "-n", "default").start();

        process.waitFor();
        return new String(process.getInputStream().readAllBytes());
    }
}