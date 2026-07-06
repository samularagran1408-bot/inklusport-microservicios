package com.inklusport.ai.service;

import com.inklusport.ai.dto.response.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationsService {

    public RecommendationResponse getRecommendations(String userId) {
        log.info("Generando recomendaciones para usuario {}", userId);
        return RecommendationResponse.builder()
                .userId(userId)
                .recommendations(java.util.List.of("Mantener hidratación", "Incrementar movilidad"))
                .build();
    }
}
