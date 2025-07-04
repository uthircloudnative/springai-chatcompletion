package com.springai.chatcompletion.service;

import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * VertexAIChatService
 *
 * @author Uthiraraj Saminathan
 */
@Service
public class VertexAIChatService {

    private final VertexAiGeminiChatModel vertexAiGeminiChatModel;

    public VertexAIChatService(VertexAiGeminiChatModel vertexAiGeminiChatModel) {
        this.vertexAiGeminiChatModel = vertexAiGeminiChatModel;
    }

    public Map<String, String> chat(String prompt) {
        return Map.of("message", vertexAiGeminiChatModel.call(prompt));
    }

}
