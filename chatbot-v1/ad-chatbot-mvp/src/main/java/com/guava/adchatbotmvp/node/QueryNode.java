package com.guava.adchatbotmvp.node;

import com.guava.adchatbotmvp.model.AdWorkflowState;
import com.guava.adchatbotmvp.prompt.PromptTemplateService;
import java.util.Locale;
import java.util.Map;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

@Component
public class QueryNode implements NodeAction<AdWorkflowState> {

    private final PromptTemplateService promptTemplateService;

    public QueryNode(PromptTemplateService promptTemplateService) {
        this.promptTemplateService = promptTemplateService;
    }

    @Override
    public Map<String, Object> apply(AdWorkflowState state) {
        String question = state.question();
        String prompt = promptTemplateService.render("query.md", Map.of("question", question));

        boolean adRelated = isAdRelated(question);
        boolean hasResult = !state.ragDocuments().isEmpty() || state.apiPayload().isPresent();
        boolean displayable = adRelated && (hasResult || state.route().name().equals("DIRECT_QUERY"));
        String reason = displayable
                ? "query -> result accepted"
                : "query -> unrelated or missing tool result";

        return Map.of(
                AdWorkflowState.DISPLAYABLE, displayable,
                AdWorkflowState.QUERY_REASON, reason,
                AdWorkflowState.QUERY_PROMPT, prompt,
                AdWorkflowState.TRACE, reason
        );
    }

    private boolean isAdRelated(String question) {
        String normalized = question.toLowerCase(Locale.ROOT);
        return normalized.contains("广告")
                || normalized.contains("roi")
                || normalized.contains("投放")
                || normalized.contains("转化")
                || normalized.contains("素材")
                || normalized.contains("点击率")
                || normalized.contains("cpm")
                || normalized.contains("ctr");
    }
}
