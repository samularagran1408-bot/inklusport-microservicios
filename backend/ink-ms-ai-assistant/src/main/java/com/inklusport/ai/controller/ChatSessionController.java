package com.inklusport.ai.controller;

import com.inklusport.ai.model.ChatSession;
import com.inklusport.ai.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/chat/sessions")
@RequiredArgsConstructor
@Tag(name = "Chat Sessions", description = "Gestión de sesiones de chat")
public class ChatSessionController {

    private final ChatService chatService;

    @Operation(summary = "Listar sesiones del usuario")
    @GetMapping
    public ResponseEntity<List<ChatSession>> getUserSessions(
            @AuthenticationPrincipal String userId) {
        return ResponseEntity.ok(chatService.getUserSessions(userId));
    }

    @Operation(summary = "Obtener sesión por ID")
    @GetMapping("/{sessionId}")
    public ResponseEntity<ChatSession> getSession(@PathVariable String sessionId) {
        return ResponseEntity.ok(chatService.getSession(sessionId));
    }

    @Operation(summary = "Cerrar sesión")
    @PutMapping("/{sessionId}/close")
    public ResponseEntity<Map<String, String>> closeSession(@PathVariable String sessionId) {
        chatService.closeSession(sessionId);
        return ResponseEntity.ok(Map.of("message", "Sesión cerrada", "sessionId", sessionId));
    }
}
