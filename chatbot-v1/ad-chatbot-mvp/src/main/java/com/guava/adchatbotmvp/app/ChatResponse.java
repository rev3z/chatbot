package com.guava.adchatbotmvp.app;

import java.util.List;
import java.util.Map;

public record ChatResponse(
        String userId,
        String question,
        String route,
        boolean displayable,
        String answer,
        List<String> trace,
        List<Map<String, Object>> ragDocuments,
        Map<String, Object> apiPayload
) {
}
