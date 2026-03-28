package com.guava.adchatbotmvp.node;

import com.guava.adchatbotmvp.model.AdMetricsSnapshot;
import com.guava.adchatbotmvp.model.AdWorkflowState;
import com.guava.adchatbotmvp.service.FakeAdsMetricsService;
import java.util.Map;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

@Component
public class ApiToolNode implements NodeAction<AdWorkflowState> {

    private final FakeAdsMetricsService adsMetricsService;

    public ApiToolNode(FakeAdsMetricsService adsMetricsService) {
        this.adsMetricsService = adsMetricsService;
    }

    @Override
    public Map<String, Object> apply(AdWorkflowState state) {
        AdMetricsSnapshot snapshot = adsMetricsService.getRecentSevenDayMetrics(state.userId());
        return Map.of(
                AdWorkflowState.API_PAYLOAD, snapshot,
                AdWorkflowState.TRACE, "api-tool -> loaded recent 7 day ROI snapshot"
        );
    }
}
