package com.guava.adchatbotmvp.graph;

import static org.bsc.langgraph4j.StateGraph.END;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.action.AsyncEdgeAction.edge_async;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;

import com.guava.adchatbotmvp.model.AdWorkflowState;
import com.guava.adchatbotmvp.node.AnswerNode;
import com.guava.adchatbotmvp.node.ApiToolNode;
import com.guava.adchatbotmvp.node.OrchestratorNode;
import com.guava.adchatbotmvp.node.QueryNode;
import com.guava.adchatbotmvp.node.RagToolNode;
import java.util.Map;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.EdgeAction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkflowConfiguration {

    @Bean
    public CompiledGraph<AdWorkflowState> adChatbotGraph(
            OrchestratorNode orchestratorNode,
            RagToolNode ragToolNode,
            ApiToolNode apiToolNode,
            QueryNode queryNode,
            AnswerNode answerNode
    ) throws Exception {
        EdgeAction<AdWorkflowState> routeAfterOrchestrator = state -> switch (state.route()) {
            case RAG_TOOL -> "rag-tool";
            case API_TOOL -> "api-tool";
            case DIRECT_QUERY -> "query";
        };

        StateGraph<AdWorkflowState> workflow = new StateGraph<>(AdWorkflowState.SCHEMA, AdWorkflowState::new)
                .addNode("orchestrator", node_async(orchestratorNode))
                .addNode("rag-tool", node_async(ragToolNode))
                .addNode("api-tool", node_async(apiToolNode))
                .addNode("query", node_async(queryNode))
                .addNode("answer", node_async(answerNode))
                .addEdge(START, "orchestrator")
                .addConditionalEdges(
                        "orchestrator",
                        edge_async(routeAfterOrchestrator),
                        Map.of(
                                "rag-tool", "rag-tool",
                                "api-tool", "api-tool",
                                "query", "query"
                        )
                )
                .addEdge("rag-tool", "query")
                .addEdge("api-tool", "query")
                .addEdge("query", "answer")
                .addEdge("answer", END);

        return workflow.compile();
    }
}
