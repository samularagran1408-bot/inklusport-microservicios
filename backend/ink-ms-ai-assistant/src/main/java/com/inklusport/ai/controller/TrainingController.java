package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.TrainingPlanRequest;
import com.inklusport.ai.dto.response.TrainingPlanResponse;
import com.inklusport.ai.service.TrainingPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/training")
@RequiredArgsConstructor
@Tag(name = "Entrenamiento", description = "Planes personalizados")
public class TrainingController {

    private final TrainingPlanService trainingPlanService;

    @Operation(summary = "Crear plan de entrenamiento personalizado")
    @PostMapping("/plan")
    public ResponseEntity<TrainingPlanResponse> createPlan(@Valid @RequestBody TrainingPlanRequest request) {
        return ResponseEntity.ok(trainingPlanService.buildPlan(request));
    }
}
