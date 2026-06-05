package com.ai.ops.service;

import com.ai.ops.model.PodStatusDto;
import com.ai.ops.model.PrometheusResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrometheusService {

    private final RestTemplate restTemplate;
    @Value("${prometheus.url}")
    private String prometheusUrl;


    public PrometheusResponse queryRaw(String promQl) {

        String url = prometheusUrl + "/api/v1/query?query=" +
                        URLEncoder.encode(promQl, StandardCharsets.UTF_8);

        return restTemplate.getForObject(URI.create(url), PrometheusResponse.class);
    }

    public double query(String promQl) {
        PrometheusResponse response = queryRaw(promQl);
        if (response == null || response.getData() == null || response.getData().getResult().isEmpty()) {
            return 0;
        }

        return Double.parseDouble(response.getData().getResult().get(0).getValue().get(1).toString());
    }

    public double getOrderServiceCpu() {
        return query("sum(rate(process_cpu_usage{application=\"order-service\"}[5m])) * 100");
    }

    public double getOrderServiceMemoryMb() {
        double bytes = query("sum(jvm_memory_used_bytes{application=\"order-service\",area=\"heap\"})");
        return bytes / 1024 / 1024;
    }

    public int getRestartCount() {
        return (int) query("sum(kube_pod_container_status_restarts_total{pod=~\"order-service.*\"})");
    }

    public PrometheusResponse getHeapByPod() {
        return queryRaw("sum(jvm_memory_used_bytes{area=\"heap\"}) by (pod)");
    }

    public PrometheusResponse getNonHeapByPod() {
        return queryRaw("sum(jvm_memory_used_bytes{area=\"nonheap\"}) by (pod)");
    }

    public PrometheusResponse getCpuByPod() {
        return queryRaw("rate(process_cpu_usage[5m])");
    }
    public List<PodStatusDto> getPodStatuses() {

        PrometheusResponse response = queryRaw(
                "kube_pod_status_phase{pod=~\"order-service.*\"} == 1"
        );
        if (response == null || response.getData() == null ||
                response.getData().getResult() == null) {
            return Collections.emptyList();
        }
        return response.getData().getResult().stream().
                map(item -> PodStatusDto.builder().
                        podName(item.getMetric().getOrDefault("pod", "UNKNOWN")).
                        phase(item.getMetric().getOrDefault("phase", "UNKNOWN")).
                        namespace(item.getMetric().getOrDefault("namespace", "default")).
                        value(Double.parseDouble(item.getValue().get(1).toString())).build()).toList();
    }
}