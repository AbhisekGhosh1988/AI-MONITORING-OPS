package com.ai.ops.service;

import com.ai.ops.model.ClusterContext;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;
    private final ClusterContextService clusterContextService;

    public String ask(String question) {
        ClusterContext context = clusterContextService.buildContext();
        String prompt = """
                You are an expert Kubernetes SRE.
                
                Answer only using the cluster data provided.
                
                Question:
                %s
                
                Cluster Context:
                CPU: %.2f%%
                Memory: %.2f MB
                Running Pods: %d
                Restarts: %d
                Alerts: %d
                
                Recent Scaling Events:
                %s
                """.formatted(question, context.getCpu(), context.getMemory(),
                context.getRunningPods(), context.getRestartCount(),
                context.getAlerts(), context.getRecentScalingEvents());

        return chatClient.prompt().user(prompt).call().content();
    }
}