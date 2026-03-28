package com.guava.adchatbotmvp.service;

import com.guava.adchatbotmvp.model.AdMetricsSnapshot;
import org.springframework.stereotype.Service;

@Service
public class FakeAdsMetricsService {

    public AdMetricsSnapshot getRecentSevenDayMetrics(String userId) {
        return new AdMetricsSnapshot(
                userId,
                "最近7天",
                1.86d,
                2350.00d,
                128,
                "ROI 近 7 天整体平稳，但最后两天略有下降，建议优先检查素材疲劳和出价波动。"
        );
    }
}
