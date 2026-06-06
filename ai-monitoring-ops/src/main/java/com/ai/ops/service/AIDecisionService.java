package com.ai.ops.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIDecisionService {

    private final   OpenRouterService openRouterService;
    private final ChatClient chatClient;

    public String analyze(String metrics) {

    String prompt = """
         You are a Kubernetes Site Reliability Engineer.

         Analyze the metrics.

         Guidelines:

         1. CPU > 80 and runningPods < 5 => SCALE_UP
         2. CPU < 20 and runningPods > 1 => SCALE_DOWN
         3. failedPods > 0 => ALERT
         4. restartCount > 3 => ALERT

         Return JSON only.

         {
           "action":"SCALE_UP",
           "replicas":5,
           "reason":"CPU utilization above 80 percent",
           "confidence":0.9
         }

         Metrics:
         """ + metrics;
        log.info("Sending prompt to OpenRouter: {}", prompt);
        return chatClient.prompt().user(prompt).call().content();
       // return openRouterService.askOpenRouter(prompt);
    }
}