package com.inklusport.ai.controller;

import com.inklusport.ai.dto.response.RecommendationResponse;
import com.inklusport.ai.service.RecommendationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recomendaciones", description = "Recomendaciones inteligentes")
public class RecommendationsController {

    private final RecommendationsService recommendationsService;

    @Operation(summary = "Obtener recomendaciones")
    @GetMapping("/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationsService.getRecommendations(userId));
    }
}
