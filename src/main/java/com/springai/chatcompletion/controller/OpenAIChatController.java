package com.springai.chatcompletion.controller;

import com.springai.chatcompletion.service.OpenAIChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;


/**
 * OpenAIChatController
 *
 * @author Uthiraraj Saminathan
 */
@RestController
@RequestMapping("/api/v1")
public class OpenAIChatController {

    private final OpenAIChatService  openAIChatService;

    public OpenAIChatController(OpenAIChatService openAIChatService) {
        this.openAIChatService = openAIChatService;
    }

    @GetMapping("/openai/chat")
    public Map<String, String> chat(@RequestParam(value="prompt") String prompt) {
        return openAIChatService.chat(prompt);
    }

    @GetMapping(value = "/openai/chat/stream", produces = org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<String> chatStream(@RequestParam(value="prompt") String prompt) {
        return openAIChatService.chatStream(prompt);
    }
}
