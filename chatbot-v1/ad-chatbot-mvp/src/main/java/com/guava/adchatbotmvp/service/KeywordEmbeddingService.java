package com.guava.adchatbotmvp.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class KeywordEmbeddingService {

    private final Map<Integer, String[]> dimensions = new LinkedHashMap<>();

    public KeywordEmbeddingService() {
        dimensions.put(0, new String[]{"roi", "投入产出比", "回报"});
        dimensions.put(1, new String[]{"ctr", "点击率", "点击"});
        dimensions.put(2, new String[]{"cpm", "曝光", "展示"});
        dimensions.put(3, new String[]{"转化", "订单", "成交"});
        dimensions.put(4, new String[]{"素材", "创意", "图片", "视频"});
        dimensions.put(5, new String[]{"定向", "人群", "受众"});
        dimensions.put(6, new String[]{"规则", "解释", "什么是", "定义"});
        dimensions.put(7, new String[]{"花费", "消耗", "成本"});
    }

    public float[] embed(String text) {
        String normalized = normalize(text);
        float[] vector = new float[dimensions.size()];
        dimensions.forEach((index, keywords) -> vector[index] = score(normalized, keywords));
        normalizeVector(vector);
        return vector;
    }

    private float score(String text, String[] keywords) {
        return (float) Arrays.stream(keywords)
                .filter(keyword -> text.contains(keyword))
                .count();
    }

    private String normalize(String text) {
        return text == null ? "" : text.toLowerCase(Locale.ROOT).replace("，", " ").replace("。", " ");
    }

    private void normalizeVector(float[] vector) {
        double norm = 0.0d;
        for (float value : vector) {
            norm += value * value;
        }
        if (norm == 0.0d) {
            return;
        }
        float scale = (float) (1.0d / Math.sqrt(norm));
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i] * scale;
        }
    }
}
