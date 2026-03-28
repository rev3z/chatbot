package com.guava.prototype.bridge.grpc;

import com.guava.prototype.bridge.mock.OpenAiChatRequest;
import com.guava.prototype.bridge.mock.OpenAiChunk;
import com.guava.prototype.bridge.mock.OpenAiMessage;
import com.guava.prototype.bridge.proto.ClientEvent;
import com.guava.prototype.bridge.proto.EventKind;
import com.guava.prototype.bridge.proto.LlmBridgeServiceGrpc;
import com.guava.prototype.bridge.proto.ModelEvent;
import com.guava.prototype.bridge.proto.PromptRequest;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class FakeLlmBridgeService extends LlmBridgeServiceGrpc.LlmBridgeServiceImplBase {

    private static final ParameterizedTypeReference<ServerSentEvent<OpenAiChunk>> OPENAI_EVENT_TYPE =
            new ParameterizedTypeReference<>() {};

    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final WebClient openAiWebClient;

    public FakeLlmBridgeService(WebClient openAiWebClient) {
        this.openAiWebClient = openAiWebClient;
    }

    @Override
    public void streamReply(PromptRequest request, StreamObserver<ModelEvent> responseObserver) {
        AtomicBoolean completed = new AtomicBoolean(false);
        executor.submit(() -> bridgeOpenAiSse(
                request.getSessionId(),
                request.getPrompt(),
                responseObserver,
                completed
        ));
    }

    @Override
    public StreamObserver<ClientEvent> chat(StreamObserver<ModelEvent> responseObserver) {
        StringBuilder collected = new StringBuilder();
        AtomicBoolean replyStarted = new AtomicBoolean(false);
        AtomicBoolean completed = new AtomicBoolean(false);

        responseObserver.onNext(baseEvent("demo-session", "bidi-start", EventKind.EVENT_KIND_MESSAGE_START, "", ""));

        return new StreamObserver<>() {
            @Override
            public void onNext(ClientEvent value) {
                if (!value.getMessage().isBlank()) {
                    collected.append(value.getMessage()).append(' ');
                    responseObserver.onNext(baseEvent(
                            safeSessionId(value.getSessionId()),
                            "client-" + System.nanoTime(),
                            EventKind.EVENT_KIND_TOKEN,
                            "[gateway->grpc] " + value.getMessage(),
                            collected.toString().trim()
                    ));
                }
                if (value.getEndOfTurn() && replyStarted.compareAndSet(false, true)) {
                    executor.submit(() -> bridgeOpenAiSse(
                            safeSessionId(value.getSessionId()),
                            collected.toString().trim(),
                            responseObserver,
                            completed
                    ));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                if (replyStarted.compareAndSet(false, true)) {
                    bridgeOpenAiSse("demo-session", "收到空上行流", responseObserver, completed);
                }
            }
        };
    }

    private void bridgeOpenAiSse(
            String sessionId,
            String prompt,
            StreamObserver<ModelEvent> responseObserver,
            AtomicBoolean completed
    ) {
        StringBuilder fullText = new StringBuilder();
        responseObserver.onNext(baseEvent(sessionId, sessionId + "-start", EventKind.EVENT_KIND_MESSAGE_START, "", ""));

        openAiWebClient.post()
                .uri("/mock-openai/v1/chat/completions")
                .bodyValue(new OpenAiChatRequest(
                        "gpt-4o-mini-compatible",
                        List.of(new OpenAiMessage("user", prompt)),
                        true
                ))
                .retrieve()
                .bodyToFlux(OPENAI_EVENT_TYPE)
                .mapNotNull(ServerSentEvent::data)
                .doOnNext(chunk -> emitChunkAsGrpc(sessionId, chunk, fullText, responseObserver, completed))
                .doOnError(responseObserver::onError)
                .blockLast();
    }

    private void emitChunkAsGrpc(
            String sessionId,
            OpenAiChunk chunk,
            StringBuilder fullText,
            StreamObserver<ModelEvent> responseObserver,
            AtomicBoolean completed
    ) {
        if (chunk.choices() == null || chunk.choices().isEmpty()) {
            return;
        }

        String delta = chunk.choices().get(0).delta() == null ? "" : chunk.choices().get(0).delta().content();
        String finishReason = chunk.choices().get(0).finish_reason();

        if (delta != null && !delta.isBlank()) {
            fullText.append(delta);
            responseObserver.onNext(baseEvent(
                    sessionId,
                    chunk.id() + "-" + System.nanoTime(),
                    EventKind.EVENT_KIND_TOKEN,
                    delta,
                    ""
            ));
        }

        if ("stop".equals(finishReason)) {
            responseObserver.onNext(baseEvent(
                    sessionId,
                    sessionId + "-end",
                    EventKind.EVENT_KIND_MESSAGE_END,
                    "",
                    fullText.toString().trim()
            ));
            if (completed.compareAndSet(false, true)) {
                responseObserver.onCompleted();
            }
        }
    }

    private String safeSessionId(String sessionId) {
        return sessionId == null || sessionId.isBlank() ? "demo-session" : sessionId;
    }

    private ModelEvent baseEvent(String sessionId, String eventId, EventKind kind, String delta, String fullText) {
        return ModelEvent.newBuilder()
                .setSessionId(sessionId)
                .setEventId(eventId)
                .setKind(kind)
                .setDelta(delta)
                .setFullText(fullText)
                .build();
    }

    @PreDestroy
    public void destroy() {
        executor.shutdownNow();
    }
}
