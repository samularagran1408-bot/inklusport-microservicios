package com.inklusport.ai.controller;

import com.inklusport.ai.dto.FeedbackRequest;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.dto.FeedbackResponse;
import com.inklusport.ai.dto.FeedbackStats;
import com.inklusport.ai.service.FeedbackService;
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

import java.util.List;

@RestController
@RequestMapping("/api/ai/feedback")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Feedback Controller", description = "API para gestión de feedback del chat")
public class FeedbackController {

    private final FeedbackService feedbackService;

    /**
     * Guardar feedback
     */
    @Operation(
        summary = "Guardar feedback",
        description = "Guarda el feedback de un usuario sobre una respuesta del chat"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Feedback guardado exitosamente",
            content = @Content(schema = @Schema(implementation = FeedbackResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "No autorizado"
        )
    })
    @PostMapping(
        value = "/save",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<FeedbackResponse> saveFeedback(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Valid @RequestBody FeedbackRequest request) {
        
        log.info("💬 Guardando feedback de usuario: {}", userId);
        
        // Asignar usuario autenticado
        request.setUsuarioId(userId);
        
        FeedbackResponse response = feedbackService.saveFeedback(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtener feedback de una conversación
     */
    @Operation(
        summary = "Obtener feedback de conversación",
        description = "Obtiene todo el feedback asociado a una conversación"
    )
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<FeedbackResponse>> getFeedbackByConversation(
            @Parameter(description = "ID de la conversación") 
            @PathVariable String conversationId) {
        
        log.info("📋 Obteniendo feedback para conversación: {}", conversationId);
        
        List<FeedbackResponse> response = 
            feedbackService.getFeedbackByConversation(conversationId);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener estadísticas de feedback
     */
    @Operation(
        summary = "Obtener estadísticas",
        description = "Obtiene estadísticas de feedback del usuario o globales"
    )
    @GetMapping("/stats")
    public ResponseEntity<FeedbackStats> getFeedbackStats(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @RequestParam(required = false) Boolean global) {
        
        log.info("📊 Obteniendo estadísticas de feedback para usuario: {}", userId);
        
        // Si global es true, obtener estadísticas globales
        String targetUserId = global != null && global ? null : userId;
        
        FeedbackStats response = feedbackService.getFeedbackStats(targetUserId);
        return ResponseEntity.ok(response);
    }
}