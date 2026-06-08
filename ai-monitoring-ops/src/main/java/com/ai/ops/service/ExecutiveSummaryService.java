package com.ai.ops.service;

import com.ai.ops.model.AnomalyDto;
import com.ai.ops.model.ExecutiveSummaryResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExecutiveSummaryService {

    private final ChatClient chatClient;
    private final HealthService healthService;
    private final RootCauseService rootCauseService;
    private final ForecastService forecastService;
    private final AnomalyDetectionService anomalyDetectionService;
    private final ObjectMapper objectMapper;

    public ExecutiveSummaryResponse generate() {

        try {
            var health = healthService.calculate();
            var rootCause = rootCauseService.analyze();
            var forecast = forecastService.forecast();
            List<AnomalyDto> anomalies = anomalyDetectionService.detect();
            String prompt = """
                You are a Senior Kubernetes SRE.
                
                Generate a concise executive summary.
                
                Health Score:
                %s
                
                Root Cause:
                %s
                
                Forecast:
                CPU 30m = %s
                Memory 30m = %s
                
                Anomalies:
                %s
                
                Return ONLY JSON:
                
                {
                  "summary":"...",
                  "riskLevel":"LOW|MEDIUM|HIGH"
                }
                """
                    .formatted(health.getScore(), rootCause, forecast.getCpu30Min(),
                            forecast.getMemory30Min(), anomalies.toString());
            String response = chatClient.prompt().user(prompt).call().content();
            String cleaned = response.replace("```json", "").
                    replace("```", "").trim();
            JsonNode json = objectMapper.readTree(cleaned);

            return ExecutiveSummaryResponse.builder().
                    summary(json.get("summary").asText()).
                    riskLevel(json.get("riskLevel").asText()).
                    healthScore(health.getScore()).build();

        } catch (Exception e) {
            return ExecutiveSummaryResponse.builder()
                    .summary("Unable to generate executive summary.").
                    riskLevel("UNKNOWN").healthScore(0).build();
        }
    }
}