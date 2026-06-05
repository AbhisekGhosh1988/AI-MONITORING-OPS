package com.ai.ops.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class PrometheusResponse {

    private String status;
    private DataWrapper data;

    @Data
    public static class DataWrapper {
        private List<Result> result;
    }
    @Data
    public static class Result {
        private Map<String, String> metric;
        private List<Object> value;
    }
}
