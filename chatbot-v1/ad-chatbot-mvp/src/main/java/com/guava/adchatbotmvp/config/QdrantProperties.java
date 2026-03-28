package com.guava.adchatbotmvp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.qdrant")
public record QdrantProperties(
        String url,
        String collection,
        int vectorSize,
        int topK,
        boolean seedOnStartup
) {
}
