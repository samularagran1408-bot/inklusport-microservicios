package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.InjuryPredictionRequest;
import com.inklusport.ai.dto.response.InjuryPredictionResponse;
import com.inklusport.ai.service.InjuryPredictionService;
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
@RequestMapping("/api/ai/injury")
@RequiredArgsConstructor
@Tag(name = "Lesiones", description = "Predicción de lesiones")
public class InjuryPredictionController {

    private final InjuryPredictionService injuryPredictionService;

    @Operation(summary = "Predecir riesgo de lesión")
    @PostMapping("/predict")
    public ResponseEntity<InjuryPredictionResponse> predict(@Valid @RequestBody InjuryPredictionRequest request) {
        return ResponseEntity.ok(injuryPredictionService.predict(request));
    }
}
