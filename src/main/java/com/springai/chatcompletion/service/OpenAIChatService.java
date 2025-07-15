package com.springai.chatcompletion.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * OpenAIChatService
 *
 * @author Uthiraraj Saminathan
 */
@Service
public class OpenAIChatService {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIChatService.class);

    private final OpenAiChatModel openAiChatModel;

    public OpenAIChatService(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    public Map<String, String> chat(String prompt) {
        logger.info("Preparing to call LLM to make Blocking call");
        return Map.of("message", openAiChatModel.call(prompt));
    }

    public Flux<String> chatStream(String prompt) {

        logger.info("Preparing to call LLM to make Non-Blocking call");
        UserMessage userMessage = new UserMessage(prompt);
        Prompt userPrompt = Prompt.builder()
                                  .messages(userMessage)
                                  .build();

        return openAiChatModel.stream(userPrompt)
                              .map(chatresponse -> {
                                  if (chatresponse != null && chatresponse.getResult() != null
                                          && chatresponse.getResult().getOutput() != null
                                          && StringUtils.hasText(chatresponse.getResult().getOutput().getText())) {
                                      logger.info("Returning the Response from LLM : {}", chatresponse.getResult().getOutput().getText());
                                      return chatresponse.getResult().getOutput().getText();
                                  } else {
                                      logger.warn("Received a null or incomplete chat response for prompt: {}", prompt);
                                      // You can either return an empty string, a default value, or throw a custom exception here.
                                      // For a streaming context, returning an empty string or handling it downstream might be appropriate.
                                      return "";
                                  }
                              });
    }
}
