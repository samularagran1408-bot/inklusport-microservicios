package com.inklusport.ai.service;

import com.inklusport.ai.dto.response.MetricsDashboardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    public MetricsDashboardResponse getDashboard(String userId) {
        log.info("Generando dashboard para usuario {}", userId);
        return MetricsDashboardResponse.builder()
                .userId(userId)
                .summary("Métricas del rendimiento disponibles")
                .build();
    }
}
