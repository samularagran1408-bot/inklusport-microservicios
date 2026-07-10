package com.inklusport.ai.service;

import com.inklusport.ai.dto.response.MetricsDashboardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final HuggingFaceService huggingFaceService;

    public MetricsDashboardResponse getDashboard(String userId) {
        log.info("Generando dashboard para usuario {}", userId);

        String summary = huggingFaceService.generate(
                "Eres un analista de datos deportivos de Inklusport.",
                "Genera un resumen ejecutivo de métricas de rendimiento para el usuario " + userId
                        + " en deportes adaptados. Incluye asistencia, progreso y participación.");

        return MetricsDashboardResponse.builder()
                .userId(userId)
                .summary(summary)
                .build();
    }
}
