package com.inklusport.ai.controller;

import com.inklusport.ai.dto.PaginationRequest;
import com.inklusport.ai.dto.PaginationResponse;
import com.inklusport.ai.dto.BiomechanicalRequest;
import com.inklusport.ai.dto.BiomechanicalResponse;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.service.BiomechanicalService;
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

@RestController
@RequestMapping("/api/ai/biomechanical")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Biomechanical Controller", description = "API para análisis biomecánico")
public class BiomechanicalController {

    private final BiomechanicalService biomechanicalService;

    /**
     * Realizar análisis biomecánico
     */
    @Operation(
        summary = "Analizar biomecánica",
        description = "Realiza un análisis biomecánico basado en métricas de movimiento"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Análisis completado",
            content = @Content(schema = @Schema(implementation = BiomechanicalResponse.class))
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
        value = "/analyze",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BiomechanicalResponse> analyze(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Valid @RequestBody BiomechanicalRequest request) {
        
        log.info("Iniciando análisis biomecánico para usuario: {}", userId);
        
        // Asignar usuario autenticado
        request.setUsuarioId(userId);
        
        BiomechanicalResponse response = biomechanicalService.analyze(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener análisis de un usuario
     */
    @Operation(
        summary = "Obtener análisis del usuario",
        description = "Obtiene todos los análisis biomecánicos de un usuario con paginación"
    )
    @GetMapping("/analyses")
    public ResponseEntity<PaginationResponse<BiomechanicalResponse>> getAnalyses(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Valid PaginationRequest pagination) {
        
        log.info("Obteniendo análisis para usuario: {}", userId);
        
        PaginationResponse<BiomechanicalResponse> response = 
            biomechanicalService.getAnalysesByUser(userId, pagination);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener estadísticas de análisis
     */
    @Operation(
        summary = "Obtener estadísticas",
        description = "Obtiene estadísticas de los análisis biomecánicos del usuario"
    )
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId) {
        
        log.info("Obteniendo estadísticas para usuario: {}", userId);
        
        Map<String, Object> stats = biomechanicalService.getAnalysisStats(userId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtener último análisis del usuario
     */
    @Operation(
        summary = "Obtener último análisis",
        description = "Obtiene el análisis biomecánico más reciente del usuario"
    )
    @GetMapping("/latest")
    public ResponseEntity<BiomechanicalResponse> getLatestAnalysis(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId) {
        
        log.info("Obteniendo último análisis para usuario: {}", userId);
        
        PaginationRequest pagination = PaginationRequest.builder()
                .page(0)
                .size(1)
                .sortBy("fecha")
                .sortDirection("DESC")
                .build();
        
        PaginationResponse<BiomechanicalResponse> response = 
            biomechanicalService.getAnalysesByUser(userId, pagination);
        
        if (response.getContent() != null && !response.getContent().isEmpty()) {
            return ResponseEntity.ok(response.getContent().get(0));
        }
        
        return ResponseEntity.noContent().build();
    }
}