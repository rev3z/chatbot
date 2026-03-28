package com.guava.adchatbotmvp.controller;

import com.guava.adchatbotmvp.app.ChatRequest;
import com.guava.adchatbotmvp.app.ChatResponse;
import com.guava.adchatbotmvp.service.ChatWorkflowService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatWorkflowService chatWorkflowService;

    public ChatController(ChatWorkflowService chatWorkflowService) {
        this.chatWorkflowService = chatWorkflowService;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatWorkflowService.chat(request);
    }
}
