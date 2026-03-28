package com.guava.adchatbotmvp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.guava.adchatbotmvp.config.QdrantProperties;
import com.guava.adchatbotmvp.model.KnowledgeDocument;
import com.guava.adchatbotmvp.model.RagDocumentHit;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class QdrantRagService {

    private static final Logger log = LoggerFactory.getLogger(QdrantRagService.class);

    private final RestClient restClient;
    private final QdrantProperties properties;
    private final KeywordEmbeddingService embeddingService;
    private final FakeKnowledgeBaseLoader fakeKnowledgeBaseLoader;

    public QdrantRagService(
            QdrantProperties properties,
            KeywordEmbeddingService embeddingService,
            FakeKnowledgeBaseLoader fakeKnowledgeBaseLoader
    ) {
        this.properties = properties;
        this.embeddingService = embeddingService;
        this.fakeKnowledgeBaseLoader = fakeKnowledgeBaseLoader;
        this.restClient = RestClient.builder()
                .baseUrl(properties.url())
                .build();
    }

    @PostConstruct
    public void initialize() {
        if (!properties.seedOnStartup()) {
            return;
        }
        try {
            ensureCollectionExists();
            seedDocuments(fakeKnowledgeBaseLoader.loadDocuments());
            log.info("Qdrant collection '{}' is ready with fake documents", properties.collection());
        } catch (RestClientResponseException exception) {
            log.warn("Failed to initialize Qdrant collection '{}': status={}, body={}",
                    properties.collection(),
                    exception.getStatusCode(),
                    exception.getResponseBodyAsString());
        } catch (RuntimeException exception) {
            log.warn("Failed to initialize Qdrant collection '{}': {}", properties.collection(), exception.getMessage());
        }
    }

    public List<RagDocumentHit> searchAndRerank(String question) {
        try {
            JsonNode response = restClient.post()
                    .uri("/collections/{collection}/points/query", properties.collection())
                    .body(Map.of(
                            "query", toVectorList(embeddingService.embed(question)),
                            "limit", properties.topK(),
                            "with_payload", true
                    ))
                    .retrieve()
                    .body(JsonNode.class);

            List<RagDocumentHit> hits = parseHits(response, question);
            return hits.stream()
                    .sorted(Comparator.comparingDouble(RagDocumentHit::rerankScore).reversed())
                    .toList();
        } catch (RestClientResponseException exception) {
            log.warn("Qdrant query failed: status={}, body={}",
                    exception.getStatusCode(),
                    exception.getResponseBodyAsString());
            return List.of();
        }
    }

    private void ensureCollectionExists() {
        try {
            restClient.get()
                    .uri("/collections/{collection}", properties.collection())
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw exception;
            }
            restClient.put()
                    .uri("/collections/{collection}", properties.collection())
                    .body(Map.of(
                            "vectors", Map.of(
                                    "size", properties.vectorSize(),
                                    "distance", "Cosine"
                            )
                    ))
                    .retrieve()
                    .toBodilessEntity();
        }
    }

    private void seedDocuments(List<KnowledgeDocument> documents) {
        AtomicInteger counter = new AtomicInteger(1);
        List<Map<String, Object>> points = documents.stream()
                .map(document -> {
                    Map<String, Object> payload = new LinkedHashMap<>();
                    payload.put("docId", document.id());
                    payload.put("title", document.title());
                    payload.put("summary", document.summary());
                    payload.put("content", document.content());
                    payload.put("keywords", document.keywords());
                    return Map.of(
                            "id", counter.getAndIncrement(),
                            "vector", toVectorList(embeddingService.embed(document.content() + " " + document.summary())),
                            "payload", payload
                    );
                })
                .toList();

        restClient.put()
                .uri("/collections/{collection}/points", properties.collection())
                .body(Map.of("points", points))
                .retrieve()
                .toBodilessEntity();
    }

    private List<RagDocumentHit> parseHits(JsonNode response, String question) {
        JsonNode points = response.path("result").path("points");
        if (!points.isArray()) {
            return List.of();
        }
        Set<String> tokens = tokenize(question);
        List<RagDocumentHit> hits = new ArrayList<>();
        for (JsonNode point : points) {
            JsonNode payload = point.path("payload");
            String content = payload.path("content").asText("");
            String summary = payload.path("summary").asText("");
            double retrievalScore = point.path("score").asDouble(0.0d);
            double rerankScore = retrievalScore + keywordBoost(tokens, payload.path("keywords"));
            hits.add(new RagDocumentHit(
                    payload.path("docId").asText("unknown"),
                    payload.path("title").asText("unknown"),
                    summary,
                    content,
                    retrievalScore,
                    rerankScore
            ));
        }
        return hits;
    }

    private double keywordBoost(Set<String> tokens, JsonNode keywords) {
        if (!keywords.isArray()) {
            return 0.0d;
        }
        int matches = 0;
        for (JsonNode keyword : keywords) {
            String normalized = keyword.asText("").toLowerCase(Locale.ROOT);
            if (tokens.contains(normalized)) {
                matches++;
            }
        }
        return matches * 0.1d;
    }

    private Set<String> tokenize(String question) {
        String normalized = question == null ? "" : question.toLowerCase(Locale.ROOT);
        return List.of(normalized.split("[\\s,，。？?]+")).stream()
                .map(String::trim)
                .filter(token -> !token.isBlank())
                .collect(Collectors.toSet());
    }

    private List<Float> toVectorList(float[] vector) {
        List<Float> values = new ArrayList<>(vector.length);
        for (float value : vector) {
            values.add(value);
        }
        return values;
    }
}
