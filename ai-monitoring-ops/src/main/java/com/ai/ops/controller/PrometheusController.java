package com.ai.ops.controller;

import com.ai.ops.model.DashboardSummary;
import com.ai.ops.model.PodStatusDto;
import com.ai.ops.model.PrometheusResponse;
import com.ai.ops.service.PrometheusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/prometheus")
public class PrometheusController {

    private final PrometheusService prometheusService;

    @GetMapping("/heap")
    public PrometheusResponse heap() {
        return prometheusService.getHeapByPod();
    }

    @GetMapping("/nonheap")
    public PrometheusResponse nonHeap() {
        return prometheusService.getNonHeapByPod();
    }

    @GetMapping("/cpu")
    public PrometheusResponse cpu() {
        return prometheusService.getCpuByPod();
    }
    @GetMapping("/podstatus")
    public List<PodStatusDto> podStatus() {
        return prometheusService.getPodStatuses();
    }

    @GetMapping("/summary")
    public DashboardSummary summary() {

        return DashboardSummary.builder()
                .cpuPercent(prometheusService.getOrderServiceCpu())
                .memoryMb(prometheusService.getOrderServiceMemoryMb())
                .restartCount(prometheusService.getRestartCount())
                .build();
    }
}