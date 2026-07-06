package com.inklusport.ai.controller;

import com.inklusport.ai.dto.AiContextRequest;
import com.inklusport.ai.dto.AiFeatureResponse;
import com.inklusport.ai.dto.BiomechanicalAnalysisRequest;
import com.inklusport.ai.dto.TrainingPlanRequest;
import com.inklusport.ai.model.BiomechanicalAnalysis;
import com.inklusport.ai.service.AiFeatureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ai/features")
@RequiredArgsConstructor
@Tag(name = "AI Features", description = "Funcionalidades de IA para deportes adaptados")
public class AiFeatureController {

    private final AiFeatureService aiFeatureService;

    @Operation(summary = "Análisis Biomecánico con IA")
    @PostMapping("/biomechanical-analysis")
    public ResponseEntity<AiFeatureResponse> biomechanicalAnalysis(
            @Valid @RequestBody BiomechanicalAnalysisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(aiFeatureService.biomechanicalAnalysis(request));
    }

    @Operation(summary = "Historial de análisis biomecánicos")
    @GetMapping("/biomechanical-analysis/user/{usuarioId}")
    public ResponseEntity<List<BiomechanicalAnalysis>> getBiomechanicalHistory(
            @PathVariable String usuarioId) {
        List<BiomechanicalAnalysis> history = aiFeatureService.getBiomechanicalHistory(usuarioId);
        if (history.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Adaptación Inteligente de Ejercicios")
    @PostMapping("/exercise-adaptation")
    public ResponseEntity<AiFeatureResponse> exerciseAdaptation(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.exerciseAdaptation(request));
    }

    @Operation(summary = "Predicción de Riesgo de Lesiones")
    @PostMapping("/injury-risk")
    public ResponseEntity<AiFeatureResponse> injuryRisk(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.injuryRiskPrediction(request));
    }

    @Operation(summary = "Planes de Entrenamiento Personalizados por IA")
    @PostMapping("/training-plan")
    public ResponseEntity<AiFeatureResponse> trainingPlan(
            @Valid @RequestBody TrainingPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(aiFeatureService.personalizedTrainingPlan(request));
    }

    @Operation(summary = "Detección de Fatiga en Tiempo Real")
    @PostMapping("/fatigue-detection")
    public ResponseEntity<AiFeatureResponse> fatigueDetection(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.fatigueDetection(request));
    }

    @Operation(summary = "Asistente por Voz Adaptado")
    @PostMapping("/voice-assistant")
    public ResponseEntity<AiFeatureResponse> voiceAssistant(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.voiceAssistant(request));
    }

    @Operation(summary = "Dashboard de Métricas Avanzadas")
    @PostMapping("/metrics-dashboard")
    public ResponseEntity<AiFeatureResponse> metricsDashboard(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.metricsDashboard(request));
    }

    @Operation(summary = "Comparativa con Historial Personal")
    @PostMapping("/history-comparison")
    public ResponseEntity<AiFeatureResponse> historyComparison(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.historyComparison(request));
    }

    @Operation(summary = "Recomendación Inteligente de Eventos")
    @PostMapping("/event-recommendations")
    public ResponseEntity<AiFeatureResponse> eventRecommendations(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.eventRecommendations(request));
    }

    @Operation(summary = "Recomendación Personalizada por Perfil")
    @PostMapping("/profile-recommendations")
    public ResponseEntity<AiFeatureResponse> profileRecommendations(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.profileRecommendations(request));
    }

    @Operation(summary = "Filtrado Inteligente de Deportes")
    @PostMapping("/sport-filtering")
    public ResponseEntity<AiFeatureResponse> sportFiltering(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.sportFiltering(request));
    }

    @Operation(summary = "Detección Automática de Discapacidad")
    @PostMapping("/disability-detection")
    public ResponseEntity<AiFeatureResponse> disabilityDetection(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.disabilityDetection(request));
    }

    @Operation(summary = "Modo Competencia con IA")
    @PostMapping("/competition-mode")
    public ResponseEntity<AiFeatureResponse> competitionMode(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.competitionMode(request));
    }

    @Operation(summary = "Integración con Wearables")
    @PostMapping("/wearables")
    public ResponseEntity<AiFeatureResponse> wearables(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.wearablesIntegration(request));
    }

    @Operation(summary = "Alertas Inteligentes para Entrenadores")
    @PostMapping("/trainer-alerts")
    public ResponseEntity<AiFeatureResponse> trainerAlerts(
            @Valid @RequestBody AiContextRequest request) {
        return ResponseEntity.ok(aiFeatureService.trainerAlerts(request));
    }
}
