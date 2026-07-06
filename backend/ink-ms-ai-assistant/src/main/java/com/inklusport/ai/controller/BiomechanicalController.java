package com.inklusport.ai.controller;

import com.inklusport.ai.dto.request.BiomechanicalRequest;
import com.inklusport.ai.dto.response.BiomechanicalResponse;
import com.inklusport.ai.service.BiomechanicalAnalysisService;
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
@RequestMapping("/api/ai/biomechanics")
@RequiredArgsConstructor
@Tag(name = "Biomecánica", description = "Análisis biomecánico")
public class BiomechanicalController {

    private final BiomechanicalAnalysisService biomechanicalAnalysisService;

    @Operation(summary = "Analizar movimiento y postura")
    @PostMapping("/analyze")
    public ResponseEntity<BiomechanicalResponse> analyze(@Valid @RequestBody BiomechanicalRequest request) {
        return ResponseEntity.ok(biomechanicalAnalysisService.analyze(request));
    }
}
