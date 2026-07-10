package com.inklusport.ai.service;

import com.inklusport.ai.dto.response.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationsService {

    private final HuggingFaceService huggingFaceService;

    public RecommendationResponse getRecommendations(String userId) {
        log.info("Generando recomendaciones para usuario {}", userId);

        String analysis = huggingFaceService.generate(
                "Eres un asesor deportivo inclusivo de Inklusport.",
                "Genera 5 recomendaciones personalizadas de deportes adaptados para el usuario " + userId
                        + ". Responde en español, una recomendación por línea.");

        List<String> recommendations = Arrays.stream(analysis.split("\n"))
                .map(String::trim)
                .filter(line -> !line.isBlank())
                .limit(5)
                .toList();

        return RecommendationResponse.builder()
                .userId(userId)
                .recommendations(recommendations.isEmpty() ? List.of(analysis) : recommendations)
                .build();
    }
}
