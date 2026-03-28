package com.guava.adchatbotmvp.service;

import com.guava.adchatbotmvp.app.ChatRequest;
import com.guava.adchatbotmvp.app.ChatResponse;
import com.guava.adchatbotmvp.model.AdMetricsSnapshot;
import com.guava.adchatbotmvp.model.AdWorkflowState;
import com.guava.adchatbotmvp.model.RagDocumentHit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.stereotype.Service;

@Service
public class ChatWorkflowService {

    private final CompiledGraph<AdWorkflowState> compiledGraph;

    public ChatWorkflowService(CompiledGraph<AdWorkflowState> compiledGraph) {
        this.compiledGraph = compiledGraph;
    }

    public ChatResponse chat(ChatRequest request) {
        AdWorkflowState finalState = compiledGraph.invoke(Map.of(
                AdWorkflowState.USER_ID, defaultUserId(request.userId()),
                AdWorkflowState.QUESTION, defaultQuestion(request.question())
        )).orElseThrow(() -> new IllegalStateException("LangGraph4j workflow returned no final state"));

        return new ChatResponse(
                finalState.userId(),
                finalState.question(),
                finalState.route().name(),
                finalState.displayable(),
                finalState.answer(),
                finalState.trace(),
                toRagDocuments(finalState.ragDocuments()),
                toApiPayload(finalState.apiPayload().orElse(null))
        );
    }

    private String defaultUserId(String userId) {
        return userId == null || userId.isBlank() ? "demo-user" : userId;
    }

    private String defaultQuestion(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("question must not be blank");
        }
        return question;
    }

    private List<Map<String, Object>> toRagDocuments(List<RagDocumentHit> hits) {
        return hits.stream().map(hit -> {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("id", hit.id());
            payload.put("title", hit.title());
            payload.put("summary", hit.summary());
            payload.put("retrievalScore", hit.retrievalScore());
            payload.put("rerankScore", hit.rerankScore());
            return payload;
        }).toList();
    }

    private Map<String, Object> toApiPayload(AdMetricsSnapshot snapshot) {
        if (snapshot == null) {
            return Map.of();
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("userId", snapshot.userId());
        payload.put("dateRange", snapshot.dateRange());
        payload.put("roi", snapshot.roi());
        payload.put("cost", snapshot.cost());
        payload.put("conversions", snapshot.conversions());
        payload.put("insight", snapshot.insight());
        return payload;
    }
}
