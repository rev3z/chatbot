package com.guava.prototype.bridge.gateway;

import com.guava.prototype.bridge.proto.ModelEvent;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
public class GatewayController {

    private final GrpcBridgeClient grpcBridgeClient;

    public GatewayController(GrpcBridgeClient grpcBridgeClient) {
        this.grpcBridgeClient = grpcBridgeClient;
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SseEventPayload>> streamAsSse(
            @RequestParam String prompt,
            @RequestParam(required = false) String sessionId
    ) {
        String effectiveSessionId = sessionId == null || sessionId.isBlank() ? UUID.randomUUID().toString() : sessionId;
        return grpcBridgeClient.streamReply(effectiveSessionId, prompt)
                .map(this::toSse);
    }

    @PostMapping(
            value = "/bridge",
            consumes = MediaType.APPLICATION_NDJSON_VALUE,
            produces = MediaType.TEXT_EVENT_STREAM_VALUE
    )
    public Flux<ServerSentEvent<SseEventPayload>> bridgeStreamableHttpToGrpc(
            @RequestBody Flux<UpstreamMessage> requestBody
    ) {
        return grpcBridgeClient.bridgeClientStream(requestBody.map(UpstreamMessage::toClientEvent))
                .map(this::toSse);
    }

    @PostMapping(
            value = "/streamable",
            consumes = MediaType.APPLICATION_NDJSON_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<StreamableHttpEvent> bridgeStreamableHttpToStreamableHttp(
            @RequestBody Flux<UpstreamMessage> requestBody
    ) {
        return grpcBridgeClient.bridgeClientStream(requestBody.map(UpstreamMessage::toClientEvent))
                .map(event -> new StreamableHttpEvent(
                        event.getSessionId(),
                        event.getEventId(),
                        event.getKind().name(),
                        event.getDelta(),
                        event.getFullText()
                ));
    }

    private ServerSentEvent<SseEventPayload> toSse(ModelEvent event) {
        return ServerSentEvent.<SseEventPayload>builder()
                .id(event.getEventId())
                .event(event.getKind().name().toLowerCase())
                .data(new SseEventPayload(
                        event.getSessionId(),
                        event.getEventId(),
                        event.getKind().name(),
                        event.getDelta(),
                        event.getFullText()
                ))
                .build();
    }
}
