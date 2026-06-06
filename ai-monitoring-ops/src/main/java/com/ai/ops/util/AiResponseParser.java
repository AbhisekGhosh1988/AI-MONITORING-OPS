package com.ai.ops.util;

import com.ai.ops.model.AIDecision;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.util.Collections;
import java.util.List;

@Slf4j
public class AiResponseParser {

    private static final ObjectMapper mapper =
            new ObjectMapper();

    public static AIDecision parse(String response) {

        try {
            int start = response.indexOf("{");
            int end = response.lastIndexOf("}");

            if (start == -1 || end == -1) {
                throw new RuntimeException("No JSON found in AI response");
            }
            String json = response.substring(start, end + 1);
            // Remove comments if AI adds them
            json = json.replaceAll("//.*", "");
            // Convert JSON -> Entity
            AIDecision alert = mapper.readValue(json, AIDecision.class);
            return alert;

        } catch (Exception ex) {
            log.error("Failed to parse AI response: {}", response, ex);
            throw new RuntimeException("Failed to parse AI response", ex);
        }
    }
}