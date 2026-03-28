package com.guava.langgraph.skeleton.adapter.grpc;

import com.guava.langgraph.interf.model.ToolCall;
import com.guava.langgraph.interf.model.ToolResult;
import com.guava.langgraph.interf.port.ToolInvoker;
import com.guava.langgraph.interf.transport.grpc.GrpcChannelProvider;
import java.util.Map;

/**
 * Skeleton adapter showing tool invocation over gRPC.
 */
public class GrpcToolInvokerAdapter implements ToolInvoker {

    private final GrpcChannelProvider channelProvider;

    public GrpcToolInvokerAdapter(GrpcChannelProvider channelProvider) {
        this.channelProvider = channelProvider;
    }

    @Override
    public ToolResult invoke(ToolCall call) {
        channelProvider.getChannel("tool-service");
        return new ToolResult(call.toolName(), true, null, Map.of("transport", "grpc"));
    }
}
