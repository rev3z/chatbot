package com.guava.langgraph.skeleton.node;

import com.guava.langgraph.interf.model.ExecutionContext;
import com.guava.langgraph.interf.model.NodeResult;
import com.guava.langgraph.interf.model.StateDelta;
import com.guava.langgraph.interf.model.ToolCall;
import com.guava.langgraph.interf.model.ToolResult;
import com.guava.langgraph.interf.port.ExecutionEventPublisher;
import com.guava.langgraph.interf.port.ToolInvoker;
import java.util.Map;

/**
 * Placeholder node for invoking a tool selected by an upstream router.
 */
public class ToolDispatchNode extends AbstractAgentNode {

    private final ToolInvoker toolInvoker;

    public ToolDispatchNode(
            ToolInvoker toolInvoker,
            ExecutionEventPublisher eventPublisher
    ) {
        super(eventPublisher);
        this.toolInvoker = toolInvoker;
    }

    @Override
    public String nodeId() {
        return "tool-dispatch";
    }

    @Override
    protected NodeResult doExecute(ExecutionContext context) {
        ToolCall toolCall = new ToolCall("placeholder-tool", Map.of(), Map.of());
        ToolResult result = toolInvoker.invoke(toolCall);
        StateDelta delta = new StateDelta(
                Map.of("toolResult", result),
                Map.of()
        );
        return new NodeResult(delta, "answer", false);
    }
}
