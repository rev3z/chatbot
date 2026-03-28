package com.guava.prototype.bridge.mock;

public record OpenAiChoice(
        int index,
        OpenAiDelta delta,
        String finish_reason
) {
}
