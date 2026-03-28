package com.guava.langgraph.skeleton.node;

import com.guava.langgraph.interf.model.ExecutionContext;
import com.guava.langgraph.interf.model.ModelRequest;
import com.guava.langgraph.interf.model.ModelResponse;
import com.guava.langgraph.interf.model.NodeResult;
import com.guava.langgraph.interf.model.StateDelta;
import com.guava.langgraph.interf.port.ExecutionEventPublisher;
import com.guava.langgraph.interf.port.ModelClient;
import java.util.List;
import java.util.Map;

/**
 * Placeholder node for final answer generation.
 */
public class AnswerNode extends AbstractAgentNode {

    private final ModelClient modelClient;

    public AnswerNode(
            ModelClient modelClient,
            ExecutionEventPublisher eventPublisher
    ) {
        super(eventPublisher);
        this.modelClient = modelClient;
    }

    @Override
    public String nodeId() {
        return "answer";
    }

    @Override
    protected NodeResult doExecute(ExecutionContext context) {
        ModelRequest request = new ModelRequest("default-model", List.of(context.userInput()), Map.of());
        ModelResponse response = modelClient.generate(request);
        StateDelta delta = new StateDelta(
                Map.of("finalAnswer", response),
                Map.of()
        );
        return new NodeResult(delta, null, true);
    }
}
