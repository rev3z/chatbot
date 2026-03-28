package com.guava.prototype.bridge.mock;

import java.util.List;

public record OpenAiChunk(
        String id,
        String object,
        List<OpenAiChoice> choices
) {
}
