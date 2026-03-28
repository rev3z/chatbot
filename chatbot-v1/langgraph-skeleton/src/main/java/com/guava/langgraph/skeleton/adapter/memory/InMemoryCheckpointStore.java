package com.guava.langgraph.skeleton.adapter.memory;

import com.guava.langgraph.interf.model.AgentState;
import com.guava.langgraph.interf.port.CheckpointStore;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCheckpointStore implements CheckpointStore {

    private final Map<String, AgentState> storage = new ConcurrentHashMap<>();

    @Override
    public void save(String executionId, String nodeId, AgentState state) {
        storage.put(executionId + ":" + nodeId, state);
    }

    @Override
    public AgentState load(String executionId, String nodeId) {
        return storage.get(executionId + ":" + nodeId);
    }
}
