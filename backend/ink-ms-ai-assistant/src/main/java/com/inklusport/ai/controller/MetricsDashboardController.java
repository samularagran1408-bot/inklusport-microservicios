package com.inklusport.ai.controller;

import com.inklusport.ai.dto.response.MetricsDashboardResponse;
import com.inklusport.ai.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai/metrics")
@RequiredArgsConstructor
@Tag(name = "Métricas", description = "Dashboard de métricas")
public class MetricsDashboardController {

    private final MetricsService metricsService;

    @Operation(summary = "Obtener dashboard de métricas")
    @GetMapping("/dashboard/{userId}")
    public ResponseEntity<MetricsDashboardResponse> getDashboard(@PathVariable String userId) {
        return ResponseEntity.ok(metricsService.getDashboard(userId));
    }
}
