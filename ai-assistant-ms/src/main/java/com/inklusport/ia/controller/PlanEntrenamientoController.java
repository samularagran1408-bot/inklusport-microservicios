package com.inklusport.ia.controller;

import com.inklusport.ia.dto.request.PlanEntrenamientoRequest;
import com.inklusport.ia.dto.response.PlanEntrenamientoResponse;
import com.inklusport.ia.service.PlanEntrenamientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Plan de entrenamiento personalizado con adaptaciones inclusivas.
 * Si ya existe plan para el mismo usuario y entrenador, se actualiza.
 */
@RestController
@RequestMapping("/api/ia/planes")
@RequiredArgsConstructor
public class PlanEntrenamientoController {

    private final PlanEntrenamientoService planEntrenamientoService;

    /**
     * Crea o actualiza un plan.
     * Cada ejercicio debe traer sus adaptaciones (voz, visual, cognitiva, etc).
     */
    @PutMapping
    public ResponseEntity<PlanEntrenamientoResponse> crearOActualizarPlan(
            @Valid @RequestBody PlanEntrenamientoRequest request) {
        return ResponseEntity.ok(planEntrenamientoService.crearOActualizarPlan(request));
    }
}
