package com.ai.ops.util;

import com.ai.ops.model.RootCauseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RootCauseParser {

    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    public static RootCauseResponse parse(String response) {

        try {

            String cleanedResponse = response
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            return MAPPER.readValue(
                    cleanedResponse,
                    RootCauseResponse.class
            );

        } catch (Exception e) {

            e.printStackTrace();

            return RootCauseResponse.builder()
                    .cause("Unable to parse response")
                    .confidence(0)
                    .recommendation("Check logs")
                    .build();
        }
    }
}