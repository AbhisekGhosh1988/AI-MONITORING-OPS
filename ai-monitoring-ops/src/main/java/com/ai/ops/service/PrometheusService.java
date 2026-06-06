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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        double cpuPercent = query(
                "process_cpu_usage{application=\"order-service\"}"
        ) * 100;

        cpuPercent = Math.round(cpuPercent * 100.0) / 100.0;
        return cpuPercent;
    }

    public double getOrderServiceMemoryMb() {
        double bytes = query("sum(jvm_memory_used_bytes{application=\"order-service\",area=\"heap\"})");
        return bytes / 1024 / 1024;
    }
    public Map<String, Integer> getPodDetails(){
        Map<String, Integer> map = new HashMap<>();
        map.put("failedPods", getFailedPods());
        map.put("restartPodCount", getPodRestartCount());
        map.put("crashBackPodCount", getPodCrashBackCount());
        map.put("runningPods", getRunningPods());

        return map;
    }
    public int getFailedPods(){
        return (int) query("sum by (pod) (\n" +
                "  max_over_time(\n" +
                "    kube_pod_status_phase{\n" +
                "      phase=\"Failed\",\n" +
                "      pod=~\"order-service.*\"\n" +
                "    }[168h]\n" +
                "  )\n" +
                ")");
    }
    public int getPodRestartCount() {
        return (int) query("sum by (pod) (\n" +
                "  increase(\n" +
                "    kube_pod_container_status_restarts_total{\n" +
                "      pod=~\"order-service.*\"\n" +
                "    }[168h]\n" +
                "  )\n" +
                ")");
    }
    public int getPodCrashBackCount() {
        return (int) query("sum by (pod) (\n" +
                "  increase(\n" +
                "    kube_pod_container_status_restarts_total{\n" +
                "      pod=~\"order-service.*\"\n" +
                "    }[168h]\n" +
                "  )\n" +
                ")");
    }

    public Integer getRunningPods() {
        return (int)
                query("count(kube_pod_status_phase{" + "phase=\"Running\"," + "pod=~\"order-service.*\"" + "})");
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