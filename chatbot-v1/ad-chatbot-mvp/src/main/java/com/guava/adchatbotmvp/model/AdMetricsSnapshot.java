package com.guava.adchatbotmvp.model;

import java.io.Serializable;

public record AdMetricsSnapshot(
        String userId,
        String dateRange,
        double roi,
        double cost,
        int conversions,
        String insight
) implements Serializable {
}
