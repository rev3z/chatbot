package com.guava.adchatbotmvp.prompt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PromptTemplateService {

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public String render(String templateName, Map<String, String> variables) {
        String template = cache.computeIfAbsent(templateName, this::loadTemplate);
        String rendered = template;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            rendered = rendered.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return rendered;
    }

    private String loadTemplate(String templateName) {
        ClassPathResource resource = new ClassPathResource("prompts/" + templateName);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load prompt template: " + templateName, exception);
        }
    }
}
