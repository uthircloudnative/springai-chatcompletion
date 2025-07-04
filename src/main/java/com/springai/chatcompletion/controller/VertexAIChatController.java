package com.springai.chatcompletion.controller;

import com.springai.chatcompletion.service.VertexAIChatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * VertexAIChatController
 *
 * @author Uthiraraj Saminathan
 */
@RestController
@RequestMapping("/api/v1")
public class VertexAIChatController {

    private final VertexAIChatService vertexAIChatService;

    public VertexAIChatController(VertexAIChatService vertexAIChatService) {
        this.vertexAIChatService = vertexAIChatService;
    }

    @GetMapping(value="/vertexai/chat")
    public Map<String,String> chat(@RequestParam(value="prompt") String prompt) {
        return vertexAIChatService.chat(prompt);
    }
}
