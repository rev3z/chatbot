package com.guava.adchatbotmvp.node;

import com.guava.adchatbotmvp.model.AdMetricsSnapshot;
import com.guava.adchatbotmvp.model.AdWorkflowState;
import com.guava.adchatbotmvp.model.RagDocumentHit;
import com.guava.adchatbotmvp.prompt.PromptTemplateService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

@Component
public class AnswerNode implements NodeAction<AdWorkflowState> {

    private final PromptTemplateService promptTemplateService;

    public AnswerNode(PromptTemplateService promptTemplateService) {
        this.promptTemplateService = promptTemplateService;
    }

    @Override
    public Map<String, Object> apply(AdWorkflowState state) {
        String prompt = promptTemplateService.render("answer.md", Map.of(
                "question", state.question(),
                "route", state.route().name(),
                "query_reason", state.queryReason()
        ));

        String answer = buildAnswer(state);
        return Map.of(
                AdWorkflowState.ANSWER, answer,
                AdWorkflowState.ANSWER_PROMPT, prompt,
                AdWorkflowState.TRACE, "answer -> response generated"
        );
    }

    private String buildAnswer(AdWorkflowState state) {
        if (!state.displayable()) {
            return "这个原型当前只处理广告相关问题。你可以继续问我 ROI、CTR、CPM、广告规则解释，或者最近 7 天投放表现。";
        }
        if (!state.ragDocuments().isEmpty()) {
            return buildRagAnswer(state.ragDocuments());
        }
        if (state.apiPayload().isPresent()) {
            return buildApiAnswer(state.apiPayload().orElseThrow());
        }
        return "目前没有拿到足够的上下文，请改问广告规则定义或最近 7 天投放数据。";
    }

    private String buildRagAnswer(List<RagDocumentHit> hits) {
        String evidence = hits.stream()
                .map(hit -> hit.title() + "（summaryScore="
                        + String.format("%.2f", hit.retrievalScore())
                        + ", rerankScore=" + String.format("%.2f", hit.rerankScore()) + "）")
                .collect(Collectors.joining("；"));
        RagDocumentHit best = hits.get(0);
        return best.content() + " 参考文档：" + evidence;
    }

    private String buildApiAnswer(AdMetricsSnapshot snapshot) {
        return String.format(
                "%s的 ROI 是 %.2f，广告花费 %.2f，转化 %d。综合判断：%s",
                snapshot.dateRange(),
                snapshot.roi(),
                snapshot.cost(),
                snapshot.conversions(),
                snapshot.insight()
        );
    }
}
