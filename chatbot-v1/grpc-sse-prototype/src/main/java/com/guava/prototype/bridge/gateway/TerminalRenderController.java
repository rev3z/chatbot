package com.guava.prototype.bridge.gateway;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/terminal")
public class TerminalRenderController {

    private final GrpcBridgeClient grpcBridgeClient;

    public TerminalRenderController(GrpcBridgeClient grpcBridgeClient) {
        this.grpcBridgeClient = grpcBridgeClient;
    }

    @PostMapping(value = "/render", consumes = MediaType.APPLICATION_NDJSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> renderAsPlainText(@RequestBody Flux<UpstreamMessage> requestBody) {
        return grpcBridgeClient.bridgeClientStream(requestBody.map(UpstreamMessage::toClientEvent))
                .filter(event -> !event.getDelta().isBlank())
                .filter(event -> !event.getDelta().startsWith("[gateway->grpc]"))
                .map(event -> event.getDelta());
    }
}
