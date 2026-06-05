package com.ai.ops.service;

import com.ai.ops.model.HealthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthService {

    private final PrometheusService prometheusService;
    private final DataSource dataSource;
    private final AIDecisionService aiDecisionService;

    public HealthResponse getHealth() {

        String application = "UP";
        String database = checkDatabase();
        String prometheus = checkPrometheus();
        String ai = checkAi();
        String kubernetes = checkKubernetes();

        return HealthResponse.builder().application(application).database(database).
                kubernetes(kubernetes).prometheus(prometheus).ai(ai).build();
    }

    private String checkDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2) ? "UP" : "DOWN";

        } catch (Exception e) {
            log.error("Database health check failed", e);
            return "DOWN";
        }
    }

    private String checkPrometheus() {
        try {
            double result = prometheusService.query("up");
            return result >= 0 ? "UP" : "DOWN";
        } catch (Exception e) {
            log.error("Prometheus health check failed", e);
            return "DOWN";
        }
    }

    private String checkAi() {
        try {
            String response = aiDecisionService.analyze("{\"test\":\"health-check\"}");
            return response != null ? "UP" : "DOWN";
        } catch (Exception e) {
            log.error("AI health check failed", e);
            return "DOWN";
        }
    }

    private String checkKubernetes() {

        try {
            prometheusService.getPodStatuses();
            return "UP";

        } catch (Exception e) {
            return "DOWN";
        }
    }
}