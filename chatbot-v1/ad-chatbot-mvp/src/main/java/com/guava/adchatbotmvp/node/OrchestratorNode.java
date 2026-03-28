package com.guava.adchatbotmvp.node;

import com.guava.adchatbotmvp.model.AdWorkflowState;
import com.guava.adchatbotmvp.model.RouteStrategy;
import com.guava.adchatbotmvp.prompt.PromptTemplateService;
import java.util.Locale;
import java.util.Map;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

@Component
public class OrchestratorNode implements NodeAction<AdWorkflowState> {

    private final PromptTemplateService promptTemplateService;

    public OrchestratorNode(PromptTemplateService promptTemplateService) {
        this.promptTemplateService = promptTemplateService;
    }

    @Override
    public Map<String, Object> apply(AdWorkflowState state) {
        String question = state.question();
        RouteStrategy route = route(question);
        String prompt = promptTemplateService.render("orchestrator.md", Map.of("question", question));
        return Map.of(
                AdWorkflowState.ROUTE, route.name(),
                AdWorkflowState.ORCHESTRATOR_PROMPT, prompt,
                AdWorkflowState.TRACE, "orchestrator -> route=" + route.name()
        );
    }

    private RouteStrategy route(String question) {
        String normalized = question.toLowerCase(Locale.ROOT);
        if (containsAny(normalized, "什么是", "解释", "介绍", "规则", "含义")) {
            return RouteStrategy.RAG_TOOL;
        }
        if (containsAny(normalized, "最近7天", "近7天", "花费", "投放数据", "表现", "趋势", "转化")) {
            return RouteStrategy.API_TOOL;
        }
        if (containsAny(normalized, "roi", "ctr", "cpm")) {
            return RouteStrategy.RAG_TOOL;
        }
        return RouteStrategy.DIRECT_QUERY;
    }

    private boolean containsAny(String question, String... keywords) {
        for (String keyword : keywords) {
            if (question.contains(keyword.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }
}
