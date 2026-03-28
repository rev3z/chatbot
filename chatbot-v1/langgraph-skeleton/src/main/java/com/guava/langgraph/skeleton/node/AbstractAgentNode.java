package com.guava.langgraph.skeleton.node;

import com.guava.langgraph.interf.api.AgentNode;
import com.guava.langgraph.interf.model.ExecutionContext;
import com.guava.langgraph.interf.model.NodeResult;
import com.guava.langgraph.interf.port.ExecutionEventPublisher;

/**
 * Base class that centralizes execution event hooks.
 */
public abstract class AbstractAgentNode implements AgentNode {

    private final ExecutionEventPublisher eventPublisher;

    protected AbstractAgentNode(ExecutionEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public final NodeResult execute(ExecutionContext context) {
        eventPublisher.publishNodeStarted(context);
        try {
            NodeResult result = doExecute(context);
            eventPublisher.publishNodeCompleted(context, result);
            return result;
        } catch (RuntimeException ex) {
            eventPublisher.publishNodeFailed(context, ex);
            throw ex;
        }
    }

    protected abstract NodeResult doExecute(ExecutionContext context);
}
