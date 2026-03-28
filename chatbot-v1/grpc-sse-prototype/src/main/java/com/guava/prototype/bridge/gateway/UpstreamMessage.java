package com.guava.prototype.bridge.gateway;

import com.guava.prototype.bridge.proto.ClientEvent;

public record UpstreamMessage(
        String sessionId,
        String message,
        boolean endOfTurn
) {
    public ClientEvent toClientEvent() {
        return ClientEvent.newBuilder()
                .setSessionId(sessionId == null ? "demo-session" : sessionId)
                .setMessage(message == null ? "" : message)
                .setEndOfTurn(endOfTurn)
                .build();
    }
}
