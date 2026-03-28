package com.guava.langgraph.skeleton.adapter.memory;

import com.guava.langgraph.interf.model.AgentState;
import com.guava.langgraph.interf.port.SessionStore;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySessionStore implements SessionStore {

    private final Map<String, AgentState> storage = new ConcurrentHashMap<>();

    @Override
    public AgentState load(String sessionId) {
        return storage.get(sessionId);
    }

    @Override
    public void save(String sessionId, AgentState state) {
        storage.put(sessionId, state);
    }
}
