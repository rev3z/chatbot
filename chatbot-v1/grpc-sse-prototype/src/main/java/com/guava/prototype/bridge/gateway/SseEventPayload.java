package com.guava.prototype.bridge.gateway;

public record SseEventPayload(
        String sessionId,
        String eventId,
        String kind,
        String delta,
        String fullText
) {
}
