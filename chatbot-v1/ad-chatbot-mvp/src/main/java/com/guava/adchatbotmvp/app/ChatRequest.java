package com.guava.adchatbotmvp.app;

public record ChatRequest(
        String userId,
        String question
) {
}
