package com.guava.adchatbotmvp.model;

import java.util.List;

public record KnowledgeDocument(
        String id,
        String title,
        String summary,
        String content,
        List<String> keywords
) {
}
