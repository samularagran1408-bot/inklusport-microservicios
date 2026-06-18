package com.inklusport.ai.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai/health")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Health Controller", description = "API para health checks y monitoreo")
public class HealthController {

    private final HealthIndicator healthCheckService;

    /**
     * Health check básico
     */
    @Operation(
        summary = "Health check",
        description = "Verifica el estado del servicio y sus dependencias"
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        log.debug("Health check solicitado");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Inklusport AI Service");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Health check detallado
     */
    @Operation(
        summary = "Health check detallado",
        description = "Verifica el estado detallado del servicio y sus dependencias"
    )
    @GetMapping("/detailed")
    public ResponseEntity<Health> healthDetailed() {
        log.debug("Health check detallado solicitado");
        
        Health health = healthCheckService.health();
        return ResponseEntity.ok(health);
    }

    /**
     * Readiness probe
     */
    @Operation(
        summary = "Readiness probe",
        description = "Verifica si el servicio está listo para recibir tráfico"
    )
    @GetMapping("/readiness")
    public ResponseEntity<Map<String, Object>> readiness() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "READY");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Liveness probe
     */
    @Operation(
        summary = "Liveness probe",
        description = "Verifica si el servicio está vivo"
    )
    @GetMapping("/liveness")
    public ResponseEntity<Map<String, Object>> liveness() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ALIVE");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
}