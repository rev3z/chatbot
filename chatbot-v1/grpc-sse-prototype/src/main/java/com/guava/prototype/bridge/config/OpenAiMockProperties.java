package com.guava.prototype.bridge.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "prototype.openai")
public record OpenAiMockProperties(String baseUrl) {
}
