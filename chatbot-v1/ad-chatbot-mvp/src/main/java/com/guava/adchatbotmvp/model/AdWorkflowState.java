package com.guava.adchatbotmvp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

public class AdWorkflowState extends AgentState {

    public static final String USER_ID = "userId";
    public static final String QUESTION = "question";
    public static final String ROUTE = "route";
    public static final String TRACE = "trace";
    public static final String DISPLAYABLE = "displayable";
    public static final String QUERY_REASON = "queryReason";
    public static final String RAG_DOCUMENTS = "ragDocuments";
    public static final String API_PAYLOAD = "apiPayload";
    public static final String ANSWER = "answer";
    public static final String ORCHESTRATOR_PROMPT = "orchestratorPrompt";
    public static final String QUERY_PROMPT = "queryPrompt";
    public static final String ANSWER_PROMPT = "answerPrompt";

    public static final Map<String, Channel<?>> SCHEMA = Map.of(
            TRACE, Channels.appender(ArrayList::new)
    );

    public AdWorkflowState(Map<String, Object> initData) {
        super(initData);
    }

    public String userId() {
        return value(USER_ID).map(String.class::cast).orElse("");
    }

    public String question() {
        return value(QUESTION).map(String.class::cast).orElse("");
    }

    public RouteStrategy route() {
        return value(ROUTE)
                .map(String.class::cast)
                .map(RouteStrategy::valueOf)
                .orElse(RouteStrategy.DIRECT_QUERY);
    }

    public boolean displayable() {
        return value(DISPLAYABLE).map(Boolean.class::cast).orElse(false);
    }

    public String queryReason() {
        return value(QUERY_REASON).map(String.class::cast).orElse("");
    }

    @SuppressWarnings("unchecked")
    public List<String> trace() {
        return value(TRACE).map(v -> (List<String>) v).orElse(List.of());
    }

    @SuppressWarnings("unchecked")
    public List<RagDocumentHit> ragDocuments() {
        return value(RAG_DOCUMENTS).map(v -> (List<RagDocumentHit>) v).orElse(List.of());
    }

    public Optional<AdMetricsSnapshot> apiPayload() {
        return value(API_PAYLOAD).map(AdMetricsSnapshot.class::cast);
    }

    public String answer() {
        return value(ANSWER).map(String.class::cast).orElse("");
    }
}
