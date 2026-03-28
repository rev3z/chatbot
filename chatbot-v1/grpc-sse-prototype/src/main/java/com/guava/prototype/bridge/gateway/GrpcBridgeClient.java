package com.guava.prototype.bridge.gateway;

import com.guava.prototype.bridge.proto.ClientEvent;
import com.guava.prototype.bridge.proto.LlmBridgeServiceGrpc;
import com.guava.prototype.bridge.proto.ModelEvent;
import com.guava.prototype.bridge.proto.PromptRequest;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import org.springframework.stereotype.Component;

@Component
public class GrpcBridgeClient {

    private final LlmBridgeServiceGrpc.LlmBridgeServiceStub asyncStub;

    public GrpcBridgeClient(ManagedChannel managedChannel) {
        this.asyncStub = LlmBridgeServiceGrpc.newStub(managedChannel);
    }

    public Flux<ModelEvent> streamReply(String sessionId, String prompt) {
        PromptRequest request = PromptRequest.newBuilder()
                .setSessionId(sessionId)
                .setPrompt(prompt)
                .build();

        return Flux.create(sink -> asyncStub.streamReply(request, new StreamObserver<>() {
            @Override
            public void onNext(ModelEvent value) {
                sink.next(value);
            }

            @Override
            public void onError(Throwable throwable) {
                sink.error(throwable);
            }

            @Override
            public void onCompleted() {
                sink.complete();
            }
        }));
    }

    public Flux<ModelEvent> bridgeClientStream(Flux<ClientEvent> clientEvents) {
        return Flux.create(sink -> {
            StreamObserver<ClientEvent> requestObserver = asyncStub.chat(new StreamObserver<>() {
                @Override
                public void onNext(ModelEvent value) {
                    sink.next(value);
                }

                @Override
                public void onError(Throwable throwable) {
                    sink.error(throwable);
                }

                @Override
                public void onCompleted() {
                    sink.complete();
                }
            });

            Disposable disposable = clientEvents.subscribe(
                    requestObserver::onNext,
                    requestObserver::onError,
                    requestObserver::onCompleted
            );

            sink.onDispose(disposable);
        });
    }
}
