package com.guava.langgraph.skeleton.adapter.grpc;

import com.guava.langgraph.interf.model.ModelChunk;
import com.guava.langgraph.interf.model.ModelRequest;
import com.guava.langgraph.interf.model.ModelResponse;
import com.guava.langgraph.interf.port.ModelClient;
import com.guava.langgraph.interf.transport.grpc.GrpcChannelProvider;
import com.guava.langgraph.interf.transport.grpc.GrpcMetadataFactory;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Skeleton adapter showing where grpc-java generated stubs would be used.
 */
public class GrpcModelClientAdapter implements ModelClient {

    private final GrpcChannelProvider channelProvider;
    private final GrpcMetadataFactory metadataFactory;

    public GrpcModelClientAdapter(
            GrpcChannelProvider channelProvider,
            GrpcMetadataFactory metadataFactory
    ) {
        this.channelProvider = channelProvider;
        this.metadataFactory = metadataFactory;
    }

    @Override
    public ModelResponse generate(ModelRequest request) {
        channelProvider.getChannel("model-service");
        return new ModelResponse("stubbed-response", Map.of("transport", "grpc"));
    }

    @Override
    public void stream(ModelRequest request, Consumer<ModelChunk> chunkConsumer) {
        channelProvider.getChannel("model-service");
        chunkConsumer.accept(new ModelChunk("stubbed-stream", true, Map.of("transport", "grpc")));
    }
}
