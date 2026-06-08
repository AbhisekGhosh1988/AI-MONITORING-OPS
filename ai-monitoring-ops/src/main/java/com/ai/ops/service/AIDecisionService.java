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
                You are an expert Kubernetes Site Reliability Engineer.
                
                Analyze the cluster metrics and recommend an action.
                
                Possible actions:
                
                SCALE_UP
                SCALE_DOWN
                ALERT
                NO_ACTION
                
                Return JSON only.
                
                {
                  "action":"SCALE_UP",
                  "replicas":5,
                  "reason":"Short summary",
                  "confidence":95,
                  "reasons":[
                      "CPU utilization exceeded threshold",
                      "Memory usage increasing",
                      "Traffic trend indicates growth"
                  ]
                }
                
                Metrics:
    """ + metrics;
        log.info("Sending prompt to OpenRouter: {}", prompt);
        return chatClient.prompt().user(prompt).call().content();
       // return openRouterService.askOpenRouter(prompt);
    }
}