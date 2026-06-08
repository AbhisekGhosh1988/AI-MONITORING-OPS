package com.ai.ops.controller;

import com.ai.ops.model.ChatRequest;
import com.ai.ops.model.ChatResponse;
import com.ai.ops.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse ask(@RequestBody ChatRequest request) {
        String answer = chatService.ask(request.getQuestion());
        return ChatResponse.builder().answer(answer).build();
    }
}