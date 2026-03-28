package com.guava.langgraph.skeleton.node;

import com.guava.langgraph.interf.model.ExecutionContext;
import com.guava.langgraph.interf.model.NodeResult;
import com.guava.langgraph.interf.model.RouteDecision;
import com.guava.langgraph.interf.model.StateDelta;
import com.guava.langgraph.interf.port.ExecutionEventPublisher;
import java.util.Map;

/**
 * Placeholder node for intent recognition.
 */
public class IntentRecognitionNode extends AbstractAgentNode {

    public IntentRecognitionNode(ExecutionEventPublisher eventPublisher) {
        super(eventPublisher);
    }

    @Override
    public String nodeId() {
        return "intent-recognition";
    }

    @Override
    protected NodeResult doExecute(ExecutionContext context) {
        RouteDecision decision = new RouteDecision("unresolved", "router", Map.of());
        StateDelta delta = new StateDelta(
                Map.of("routeDecision", decision),
                Map.of()
        );
        return new NodeResult(delta, "router", false);
    }
}
