package com.guava.prototype.bridge.mock;

import java.util.List;

public record OpenAiChatRequest(
        String model,
        List<OpenAiMessage> messages,
        boolean stream
) {
}
