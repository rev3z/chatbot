package com.guava.prototype.bridge.mock;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/mock-openai/v1")
public class OpenAiMockController {

    @PostMapping(value = "/chat/completions", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<OpenAiChunk>> chatCompletions(@RequestBody OpenAiChatRequest request) {
        String prompt = request.messages() == null || request.messages().isEmpty()
                ? ""
                : request.messages().get(request.messages().size() - 1).content();
        String answer = "这是来自 OpenAI 兼容 SSE 的流式回复，用于验证 gRPC 和 Streamable HTTP 的桥接。你的输入是: " + prompt;
        String completionId = "chatcmpl-" + UUID.randomUUID();

        Flux<ServerSentEvent<OpenAiChunk>> tokenFlux = Flux.fromIterable(List.of(answer.split(" ")))
                .delayElements(Duration.ofMillis(160))
                .map(token -> ServerSentEvent.<OpenAiChunk>builder()
                        .event("message")
                        .data(new OpenAiChunk(
                                completionId,
                                "chat.completion.chunk",
                                List.of(new OpenAiChoice(0, new OpenAiDelta(token + " "), null))
                        ))
                        .build());

        ServerSentEvent<OpenAiChunk> doneEvent = ServerSentEvent.<OpenAiChunk>builder()
                .event("message")
                .data(new OpenAiChunk(
                        completionId,
                        "chat.completion.chunk",
                        List.of(new OpenAiChoice(0, new OpenAiDelta(""), "stop"))
                ))
                .build();

        return Flux.concat(tokenFlux, Flux.just(doneEvent));
    }
}
