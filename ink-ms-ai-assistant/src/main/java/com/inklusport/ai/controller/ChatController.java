package com.inklusport.ai.controller;

import com.inklusport.ai.dto.PaginationRequest;
import com.inklusport.ai.dto.PaginationResponse;
import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.dto.ChatSessionResponse;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ai/chat")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Chat Controller", description = "API para gestión de chat y conversaciones")
public class ChatController {

    private final ChatService chatService;

    /**
     * Procesar mensaje de chat
     */
    @Operation(
        summary = "Procesar mensaje de chat",
        description = "Envía un mensaje al chatbot y recibe una respuesta adaptada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Mensaje procesado exitosamente",
            content = @Content(schema = @Schema(implementation = ChatResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Petición inválida",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping(
        value = "/message",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CompletableFuture<ChatResponse>> processMessage(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Valid @RequestBody ChatRequest request) {
        
        log.info("Recibiendo mensaje de usuario: {}", userId);
        
        // Si el userId viene del token, usarlo
        if (userId != null && !userId.isEmpty()) {
            request.setUserId(userId);
        }
        
        CompletableFuture<ChatResponse> response = chatService.processMessage(
            userId != null ? userId : request.getUserId(),
            request
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener sesiones de un usuario
     */
    @Operation(
        summary = "Obtener sesiones de chat",
        description = "Obtiene todas las sesiones de chat de un usuario con paginación"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sesiones obtenidas exitosamente",
            content = @Content(schema = @Schema(implementation = PaginationResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    @GetMapping("/sessions")
    public ResponseEntity<PaginationResponse<ChatSessionResponse>> getSessions(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Valid PaginationRequest pagination) {
        
        log.info("Obteniendo sesiones para usuario: {}", userId);
        
        PaginationResponse<ChatSessionResponse> response = 
            chatService.getUserSessions(userId, pagination);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener una sesión específica
     */
    @Operation(
        summary = "Obtener sesión específica",
        description = "Obtiene los detalles de una sesión de chat por su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sesión encontrada",
            content = @Content(schema = @Schema(implementation = ChatSessionResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sesión no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/sessions/{sessionId}")
    public ResponseEntity<ChatSessionResponse> getSession(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Parameter(description = "ID de la sesión") @PathVariable String sessionId) {
        
        log.info("Obteniendo sesión: {} para usuario: {}", sessionId, userId);
        
        ChatSessionResponse response = chatService.getSession(sessionId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Cerrar una sesión
     */
    @Operation(
        summary = "Cerrar sesión",
        description = "Finaliza una sesión de chat activa"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Sesión cerrada exitosamente",
            content = @Content(schema = @Schema(implementation = ChatSessionResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Sesión no encontrada"
        )
    })
    @PutMapping("/sessions/{sessionId}/close")
    public ResponseEntity<ChatSessionResponse> closeSession(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Parameter(description = "ID de la sesión") @PathVariable String sessionId) {
        
        log.info("Cerrando sesión: {} para usuario: {}", sessionId, userId);
        
        ChatSessionResponse response = chatService.closeSession(sessionId, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener estadísticas del chat
     */
    @Operation(
        summary = "Obtener estadísticas del chat",
        description = "Obtiene estadísticas de uso del chat para un usuario"
    )
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getChatStats(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId) {
        
        log.info("Obteniendo estadísticas de chat para usuario: {}", userId);
        
        Map<String, Object> stats = chatService.getChatStats(userId);
        return ResponseEntity.ok(stats);
    }
}