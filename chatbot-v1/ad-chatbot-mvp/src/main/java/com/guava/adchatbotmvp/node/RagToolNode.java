package com.guava.adchatbotmvp.node;

import com.guava.adchatbotmvp.model.AdWorkflowState;
import com.guava.adchatbotmvp.model.RagDocumentHit;
import com.guava.adchatbotmvp.service.QdrantRagService;
import java.util.List;
import java.util.Map;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

@Component
public class RagToolNode implements NodeAction<AdWorkflowState> {

    private final QdrantRagService ragService;

    public RagToolNode(QdrantRagService ragService) {
        this.ragService = ragService;
    }

    @Override
    public Map<String, Object> apply(AdWorkflowState state) {
        List<RagDocumentHit> hits = ragService.searchAndRerank(state.question());
        return Map.of(
                AdWorkflowState.RAG_DOCUMENTS, hits,
                AdWorkflowState.TRACE, "rag-tool -> retrieved=" + hits.size()
        );
    }
}
