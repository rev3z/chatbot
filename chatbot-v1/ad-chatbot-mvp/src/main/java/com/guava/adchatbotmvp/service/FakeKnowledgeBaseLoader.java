package com.guava.adchatbotmvp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guava.adchatbotmvp.model.KnowledgeDocument;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class FakeKnowledgeBaseLoader {

    private final ObjectMapper objectMapper;

    public FakeKnowledgeBaseLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<KnowledgeDocument> loadDocuments() {
        ClassPathResource resource = new ClassPathResource("rag/fake-documents.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {
            });
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load fake RAG documents", exception);
        }
    }
}
