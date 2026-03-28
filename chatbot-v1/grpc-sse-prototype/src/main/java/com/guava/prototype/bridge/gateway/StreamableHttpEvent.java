package com.guava.prototype.bridge.gateway;

public record StreamableHttpEvent(
        String sessionId,
        String eventId,
        String kind,
        String delta,
        String fullText
) {
}
