package com.ai.ops.service;

import com.ai.ops.model.ClusterContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RootCauseService {

    private final ClusterContextService clusterContextService;
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public String analyze() {

        try {

            ClusterContext context =
                    clusterContextService.buildContext();

            String contextJson =
                    objectMapper.writeValueAsString(context);

            String prompt = """
You are an expert Kubernetes Site Reliability Engineer.

IMPORTANT:
Return ONLY valid JSON.
Do NOT use markdown.
Do NOT use ```json.
Do NOT add explanations.

{
  "cause":"...",
  "confidence":85,
  "recommendation":"..."
}

Cluster Context:
""" + contextJson;

            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

        } catch (Exception e) {

            log.error("Root cause analysis failed", e);

            return """
                    {
                      "cause":"Unable to determine root cause",
                      "confidence":0,
                      "recommendation":"Check logs"
                    }
                    """;
        }
    }
}
