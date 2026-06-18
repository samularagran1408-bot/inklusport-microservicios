package com.inklusport.ai.controller;

import com.inklusport.ai.service.MetricsService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.Search;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ai/metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Metrics Controller", description = "API para métricas y monitoreo")
public class MetricsController {

    private final MetricsService metricsService;
    private final MeterRegistry meterRegistry;

    /**
     * Obtener métricas globales
     */
    @Operation(
        summary = "Obtener métricas globales",
        description = "Obtiene métricas globales del servicio"
    )
    @GetMapping("/global")
    public ResponseEntity<Map<String, Object>> getGlobalMetrics() {
        log.debug("Obteniendo métricas globales");
        
        Map<String, Object> response = new HashMap<>();
        response.put("metrics", metricsService.getGlobalMetrics());
        response.put("timestamp", java.time.LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener métricas de un usuario
     */
    @Operation(
        summary = "Obtener métricas de usuario",
        description = "Obtiene métricas específicas de un usuario"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Long>> getUserMetrics(
            @PathVariable String userId) {
        
        log.debug("Obteniendo métricas para usuario: {}", userId);
        
        Map<String, Long> response = metricsService.getUserStats(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener métricas propias del usuario autenticado
     */
    @Operation(
        summary = "Obtener métricas propias",
        description = "Obtiene métricas del usuario autenticado"
    )
    @GetMapping("/me")
    public ResponseEntity<Map<String, Long>> getMyMetrics(
            @AuthenticationPrincipal String userId) {
        
        log.debug("Obteniendo métricas para usuario autenticado: {}", userId);
        
        Map<String, Long> response = metricsService.getUserStats(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtener métricas de Prometheus
     */
    @Operation(
        summary = "Obtener métricas Prometheus",
        description = "Obtiene métricas en formato Prometheus"
    )
    @GetMapping("/prometheus")
    public ResponseEntity<String> getPrometheusMetrics() {
        log.debug("Obteniendo métricas Prometheus");
        
        String metrics = Search.in(meterRegistry)
                .meters()
                .stream()
                .map(meter -> {
                    return meter.getId().getName() + " " + 
                           meter.measure().iterator().next().getValue();
                })
                .collect(Collectors.joining("\n"));
        
        return ResponseEntity.ok(metrics);
    }
}