package com.springai.chatcompletion.service;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    public Flux<String> chatStream(String prompt) {

        UserMessage userMessage = new UserMessage(prompt);
        Prompt userPrompt = Prompt.builder()
                                .messages(userMessage)
                                .build();

        return vertexAiGeminiChatModel.stream(userPrompt)
                                      .map(chatresponse -> chatresponse.getResult().getOutput().getText());
    }



}
