package com.inklusport.ai.controller;

import com.inklusport.ai.service.GeminiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    private final GeminiService geminiService;

    @Operation(summary = "Verificar estado del servicio")
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Inklusport AI Assistant");
        response.put("version", "2.1.0");
        response.put("geminiConfigured", geminiService.isConfigured());
        response.put("timestamp", LocalDateTime.now().toString());
        if (!geminiService.isConfigured()) {
            response.put("warning", "GEMINI_API_KEY no configurada o inválida. Obtén una en https://aistudio.google.com/apikey");
        }
        return ResponseEntity.ok(response);
    }
}
