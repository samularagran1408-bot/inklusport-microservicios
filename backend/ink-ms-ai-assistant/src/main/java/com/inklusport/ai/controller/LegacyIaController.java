package com.inklusport.ai.controller;

import com.inklusport.ai.dto.AiContextRequest;
import com.inklusport.ai.dto.AiFeatureResponse;
import com.inklusport.ai.dto.BiomechanicalAnalysisRequest;
import com.inklusport.ai.dto.ChatRequest;
import com.inklusport.ai.dto.ChatResponse;
import com.inklusport.ai.dto.TrainingPlanRequest;
import com.inklusport.ai.model.BiomechanicalAnalysis;
import com.inklusport.ai.service.AiFeatureService;
import com.inklusport.ai.service.ChatService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rutas legacy /api/ia/** para compatibilidad con documentación y gateway existente.
 */
@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
@Hidden
public class LegacyIaController {

    private final AiFeatureService aiFeatureService;
    private final ChatService chatService;

    @PostMapping("/analisis")
    public ResponseEntity<AiFeatureResponse> analisis(
            @Valid @RequestBody BiomechanicalAnalysisRequest request) {
        return ResponseEntity.status(201).body(aiFeatureService.biomechanicalAnalysis(request));
    }

    @GetMapping("/analisis/usuario/{usuarioId}")
    public ResponseEntity<List<BiomechanicalAnalysis>> historial(@PathVariable String usuarioId) {
        List<BiomechanicalAnalysis> history = aiFeatureService.getBiomechanicalHistory(usuarioId);
        if (history.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }

    @PutMapping("/planes")
    public ResponseEntity<AiFeatureResponse> planes(@Valid @RequestBody TrainingPlanRequest request) {
        return ResponseEntity.ok(aiFeatureService.personalizedTrainingPlan(request));
    }

    @PostMapping("/chat")
    public ResponseEntity<Map<String, Object>> chat(@Valid @RequestBody Map<String, String> body) {
        ChatRequest request = ChatRequest.builder()
                .message(body.get("mensaje"))
                .userId(body.get("usuarioId"))
                .build();

        ChatResponse response = chatService.processMessage(body.get("usuarioId"), request);

        Map<String, Object> legacy = new HashMap<>();
        legacy.put("conversacionId", response.getSessionId());
        legacy.put("usuarioId", body.get("usuarioId"));
        legacy.put("mensajeUsuario", body.get("mensaje"));
        legacy.put("respuestaBot", response.getResponse());
        legacy.put("estadoConversacion", "ACTIVA");
        legacy.put("updatedAt", response.getTimestamp());
        return ResponseEntity.ok(legacy);
    }

    @PostMapping("/adaptacion-ejercicios")
    public ResponseEntity<AiFeatureResponse> adaptacion(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.exerciseAdaptation(request));
    }

    @PostMapping("/riesgo-lesiones")
    public ResponseEntity<AiFeatureResponse> riesgoLesiones(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.injuryRiskPrediction(request));
    }

    @PostMapping("/fatiga")
    public ResponseEntity<AiFeatureResponse> fatiga(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.fatigueDetection(request));
    }

    @PostMapping("/voz")
    public ResponseEntity<AiFeatureResponse> voz(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.voiceAssistant(request));
    }

    @PostMapping("/metricas")
    public ResponseEntity<AiFeatureResponse> metricas(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.metricsDashboard(request));
    }

    @PostMapping("/comparativa")
    public ResponseEntity<AiFeatureResponse> comparativa(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.historyComparison(request));
    }

    @PostMapping("/eventos")
    public ResponseEntity<AiFeatureResponse> eventos(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.eventRecommendations(request));
    }

    @PostMapping("/recomendaciones")
    public ResponseEntity<AiFeatureResponse> recomendaciones(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.profileRecommendations(request));
    }

    @PostMapping("/deportes")
    public ResponseEntity<AiFeatureResponse> deportes(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.sportFiltering(request));
    }

    @PostMapping("/discapacidad")
    public ResponseEntity<AiFeatureResponse> discapacidad(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.disabilityDetection(request));
    }

    @PostMapping("/competencia")
    public ResponseEntity<AiFeatureResponse> competencia(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.competitionMode(request));
    }

    @PostMapping("/wearables")
    public ResponseEntity<AiFeatureResponse> wearables(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.wearablesIntegration(request));
    }

    @PostMapping("/alertas-entrenador")
    public ResponseEntity<AiFeatureResponse> alertas(@Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.trainerAlerts(request));
    }
}
