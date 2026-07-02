package com.inklusport.ai.controller;

import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat", description = "Endpoints del chatbot")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Enviar mensaje al chatbot")
    @PostMapping(value = "/message", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatResponse> processMessage(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody ChatRequest request) {

        log.info("Recibiendo mensaje de usuario: {}", userId);

        if (userId != null && !userId.isEmpty()) {
            request.setUserId(userId);
        }

        ChatResponse response = chatService.processMessage(
                userId != null ? userId : request.getUserId(),
                request
        );

        return ResponseEntity.ok(response);
    }
}