package com.guava.adchatbotmvp.model;

import java.io.Serializable;

public record RagDocumentHit(
        String id,
        String title,
        String summary,
        String content,
        double retrievalScore,
        double rerankScore
) implements Serializable {
}
