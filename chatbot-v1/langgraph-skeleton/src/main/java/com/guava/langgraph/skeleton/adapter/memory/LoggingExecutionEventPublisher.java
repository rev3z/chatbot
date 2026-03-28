package com.guava.langgraph.skeleton.adapter.memory;

import com.guava.langgraph.interf.model.ExecutionContext;
import com.guava.langgraph.interf.model.NodeResult;
import com.guava.langgraph.interf.port.ExecutionEventPublisher;

/**
 * Placeholder publisher. In production this could emit logs, metrics, traces, or events.
 */
public class LoggingExecutionEventPublisher implements ExecutionEventPublisher {

    @Override
    public void publishNodeStarted(ExecutionContext context) {
        // no-op
    }

    @Override
    public void publishNodeCompleted(ExecutionContext context, NodeResult result) {
        // no-op
    }

    @Override
    public void publishNodeFailed(ExecutionContext context, Throwable error) {
        // no-op
    }
}
