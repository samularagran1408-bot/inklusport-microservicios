package com.inklusport.ai.controller;

import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<?> chat(@AuthenticationPrincipal String userId,
                                   @Valid @RequestBody ChatRequest request) {
        try {
           ChatResponse response =  chatService.processMessage(userId, request);
           return ResponseEntity.ok(response);
        } catch (Exception e) {
            return buildErrorResponse(e, "/api/ai/chat");
        }
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, String path) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(e.getMessage())
                .path(path)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
