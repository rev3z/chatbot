package com.guava.langgraph.skeleton.config;

import com.guava.langgraph.interf.port.CheckpointStore;
import com.guava.langgraph.interf.port.ExecutionEventPublisher;
import com.guava.langgraph.interf.port.ModelClient;
import com.guava.langgraph.interf.port.SessionStore;
import com.guava.langgraph.interf.port.SkillExecutor;
import com.guava.langgraph.interf.port.ToolInvoker;

/**
 * Spring-style assembly facade.
 * In a real project this could become a @Configuration class.
 */
public record LangGraphRuntimeConfig(
        ModelClient modelClient,
        ToolInvoker toolInvoker,
        SkillExecutor skillExecutor,
        SessionStore sessionStore,
        CheckpointStore checkpointStore,
        ExecutionEventPublisher eventPublisher
) {
}
