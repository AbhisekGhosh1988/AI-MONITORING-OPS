package com.ai.ops.service;



import com.ai.ops.client.AbstractAIClient;
import com.ai.ops.model.OpenRouterChatRequest;
import com.ai.ops.model.OpenRouterMessage;
import com.ai.ops.model.OpenRouterReasoning;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class OpenRouterService extends AbstractAIClient {


    @Value("${openrouter.api-key}")
    private String apiKey;
    @Value("${openrouter.base-url}")
    private String openRouterUrl;
    @Value("${openrouter.model}")
    private String openRouterModel;

    public OpenRouterService(RestTemplate restTemplate) {
        super(restTemplate);
    }

    public String askOpenRouter(String prompt) {

        // Message
        OpenRouterMessage message = OpenRouterMessage.builder().role("user").content(prompt).build();
        // Reasoning
        OpenRouterReasoning reasoning = OpenRouterReasoning.builder().enabled(true).build();

        // Request
        OpenRouterChatRequest request = OpenRouterChatRequest.builder().model(openRouterModel).
                messages(List.of(message)).reasoning(reasoning).build();

        // Headers
        return sendAndExtract(openRouterUrl, apiKey, request);
    }

    @Override
    protected String getServiceName() {
        return "OpenRouter";
    }

    @Override
    protected String extractResponse(Map responseBody) {
        try {
            // OpenRouter response format: { choices: [{ message: { content: "..." } }] }
            List<?> choices = (List<?>) responseBody.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<?, ?> choice = (Map<?, ?>) choices.get(0);
                Map<?, ?> message = (Map<?, ?>) choice.get("message");
                return (String) message.get("content");
            }
            throw new RuntimeException("Invalid OpenRouter response format");
        } catch (Exception e) {
            log.error("Failed to extract OpenRouter response", e);
            throw new RuntimeException("Failed to parse OpenRouter response: " + e.getMessage(), e);
        }
    }

    @Override
    protected void addCustomHeaders(HttpHeaders headers) {
        // Optional: Add any OpenRouter-specific headers if needed
        // Example: headers.add("HTTP-Referer", "your-website.com");
    }
}