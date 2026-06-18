package com.inklusport.ai.controller;

import com.inklusport.ai.dto.PaginationRequest;
import com.inklusport.ai.dto.PaginationResponse;
import com.inklusport.ai.dto.TrainingPlanRequest;
import com.inklusport.ai.dto.ErrorResponse;
import com.inklusport.ai.dto.TrainingPlanResponse;
import com.inklusport.ai.service.TrainingPlanService;
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

@RestController
@RequestMapping("/api/ai/training-plans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Training Plan Controller", description = "API para gestión de planes de entrenamiento")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    /**
     * Crear plan de entrenamiento
     */
    @Operation(
        summary = "Crear plan de entrenamiento",
        description = "Crea un nuevo plan de entrenamiento personalizado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Plan creado exitosamente",
            content = @Content(schema = @Schema(implementation = TrainingPlanResponse.class))
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
        value = "/create",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TrainingPlanResponse> createPlan(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Valid @RequestBody TrainingPlanRequest request) {
        
        log.info("Creando plan de entrenamiento para usuario: {}", userId);
        
        // Asignar usuario autenticado
        request.setUsuarioId(userId);
        
        TrainingPlanResponse response = trainingPlanService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Obtener plan activo del usuario
     */
    @Operation(
        summary = "Obtener plan activo",
        description = "Obtiene el plan de entrenamiento activo del usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Plan encontrado",
            content = @Content(schema = @Schema(implementation = TrainingPlanResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No hay plan activo",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/active")
    public ResponseEntity<TrainingPlanResponse> getActivePlan(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId) {
        
        log.info("Obteniendo plan activo para usuario: {}", userId);
        
        TrainingPlanResponse response = trainingPlanService.getActivePlan(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener todos los planes del usuario
     */
    @Operation(
        summary = "Obtener todos los planes",
        description = "Obtiene todos los planes de entrenamiento del usuario con paginación"
    )
    @GetMapping
    public ResponseEntity<PaginationResponse<TrainingPlanResponse>> getUserPlans(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Valid PaginationRequest pagination) {
        
        log.info("Obteniendo planes para usuario: {}", userId);
        
        PaginationResponse<TrainingPlanResponse> response = 
            trainingPlanService.getUserPlans(userId, pagination);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Actualizar progreso
     */
    @Operation(
        summary = "Actualizar progreso",
        description = "Actualiza el progreso del plan de entrenamiento activo del usuario"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Progreso actualizado",
            content = @Content(schema = @Schema(implementation = TrainingPlanResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Valor de progreso inválido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No hay plan activo"
        )
    })
    @PatchMapping("/progress")
    public ResponseEntity<TrainingPlanResponse> updateProgress(
            @Parameter(hidden = true) @AuthenticationPrincipal String userId,
            @Parameter(description = "Porcentaje de progreso (0-100)") 
            @RequestParam Double progress) {
        
        log.info("Actualizando progreso para usuario: {} a {}%", userId, progress);
        
        TrainingPlanResponse response = trainingPlanService.updateProgress(userId, progress);
        return ResponseEntity.ok(response);
    }
}