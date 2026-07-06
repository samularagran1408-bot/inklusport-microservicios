package com.inklusport.ai.service;

import com.inklusport.ai.dto.AiContextRequest;
import com.inklusport.ai.dto.AiFeatureResponse;
import com.inklusport.ai.dto.BiomechanicalAnalysisRequest;
import com.inklusport.ai.dto.TrainingPlanRequest;
import com.inklusport.ai.model.BiomechanicalAnalysis;
import com.inklusport.ai.model.TrainingPlan;
import com.inklusport.ai.repository.BiomechanicalAnalysisRepository;
import com.inklusport.ai.repository.TrainingPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AiFeatureService {

    private final GeminiService geminiService;
    private final BiomechanicalAnalysisRepository biomechanicalRepository;
    private final TrainingPlanRepository trainingPlanRepository;

    public AiFeatureResponse biomechanicalAnalysis(BiomechanicalAnalysisRequest req) {
        double puntaje = (req.getRangoMovimiento() * 0.4)
                + (req.getSimetria() * 0.3)
                + (req.getEstabilidad() * 0.3);

        String prompt = String.format("""
                Analiza biomecánicamente estos datos de un deportista con discapacidad adaptada:
                - Tipo discapacidad: %s
                - Rango de movimiento: %d/100
                - Simetría: %d/100
                - Estabilidad: %d/100
                - Puntaje calculado: %.1f/100
                - Deporte practicado: %s
                - Notas: %s

                Proporciona: 1) Evaluación general, 2) Áreas de mejora, 3) 5 recomendaciones específicas,
                4) Ejercicios correctivos sugeridos. Responde en español, formato claro.
                """,
                nullSafe(req.getTipoDiscapacidad(), "No especificada"),
                req.getRangoMovimiento(), req.getSimetria(), req.getEstabilidad(), puntaje,
                nullSafe(req.getDeportePracticado(), "General"),
                nullSafe(req.getNotasAdicionales(), "Ninguna"));

        String aiAnalysis = geminiService.generate(
                "Eres un especialista en biomecánica deportiva adaptada para personas con discapacidad.",
                prompt);

        List<String> recomendaciones = extractRecommendations(aiAnalysis);

        BiomechanicalAnalysis saved = biomechanicalRepository.save(BiomechanicalAnalysis.builder()
                .usuarioId(req.getUsuarioId())
                .tipoDiscapacidad(req.getTipoDiscapacidad())
                .rangoMovimiento(req.getRangoMovimiento())
                .simetria(req.getSimetria())
                .estabilidad(req.getEstabilidad())
                .puntaje(puntaje)
                .recomendaciones(recomendaciones)
                .analisisIA(aiAnalysis)
                .createdAt(LocalDateTime.now())
                .build());

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("puntaje", puntaje);
        metadata.put("analysisId", saved.getId());
        metadata.put("recomendaciones", recomendaciones);

        return buildResponse("biomechanical_analysis", req.getUsuarioId(), aiAnalysis, metadata);
    }

    public List<BiomechanicalAnalysis> getBiomechanicalHistory(String usuarioId) {
        return biomechanicalRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
    }

    public AiFeatureResponse exerciseAdaptation(AiContextRequest req) {
        String prompt = buildContextPrompt(req, "ejercicio a adaptar");
        return buildResponse("exercise_adaptation", req.getUsuarioId(),
                geminiService.generate(
                        "Eres experto en adaptación de ejercicios para deportes inclusivos. " +
                        "Sugiere modificaciones, equipamiento adaptado y progresiones seguras.",
                        prompt));
    }

    public AiFeatureResponse injuryRiskPrediction(AiContextRequest req) {
        String prompt = buildContextPrompt(req, "datos del deportista");
        return buildResponse("injury_prediction", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un fisioterapeuta deportivo especializado en prevención de lesiones " +
                        "en deportistas con discapacidad. Evalúa riesgos y da recomendaciones preventivas.",
                        prompt));
    }

    public AiFeatureResponse personalizedTrainingPlan(TrainingPlanRequest req) {
        String prompt = String.format("""
                Crea un plan de entrenamiento personalizado:
                - Usuario: %s
                - Discapacidad: %s
                - Objetivo: %s
                - Duración: %d semanas
                - Deportes preferidos: %s
                - Restricciones: %s

                Incluye: calentamiento, ejercicios principales con adaptaciones, descansos,
                progresión semanal y recomendaciones de seguridad. Formato estructurado en español.
                """,
                req.getUsuarioId(),
                nullSafe(req.getTipoDiscapacidad(), "No especificada"),
                nullSafe(req.getObjetivo(), "Mejora general"),
                req.getSemanasDuracion() != null ? req.getSemanasDuracion() : 4,
                joinList(req.getDeportesPreferidos()),
                joinList(req.getRestricciones()));

        String plan = geminiService.generate(
                "Eres un entrenador certificado en deportes adaptados e inclusivos.",
                prompt);

        TrainingPlan existing = trainingPlanRepository
                .findByUsuarioIdAndEntrenadorId(req.getUsuarioId(), req.getEntrenadorId())
                .orElse(null);

        TrainingPlan saved;
        if (existing != null) {
            existing.setPlanGenerado(plan);
            existing.setObjetivo(req.getObjetivo());
            existing.setUpdatedAt(LocalDateTime.now());
            saved = trainingPlanRepository.save(existing);
        } else {
            saved = trainingPlanRepository.save(TrainingPlan.builder()
                    .usuarioId(req.getUsuarioId())
                    .entrenadorId(req.getEntrenadorId())
                    .tipoDiscapacidad(req.getTipoDiscapacidad())
                    .objetivo(req.getObjetivo())
                    .planGenerado(plan)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build());
        }

        Map<String, Object> metadata = Map.of("planId", saved.getId());
        return buildResponse("training_plan", req.getUsuarioId(), plan, metadata);
    }

    public AiFeatureResponse fatigueDetection(AiContextRequest req) {
        return buildResponse("fatigue_detection", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un monitor de rendimiento deportivo. Analiza datos de fatiga en tiempo real " +
                        "(frecuencia cardíaca, velocidad, repeticiones, RPE) y determina nivel de fatiga.",
                        buildContextPrompt(req, "datos de rendimiento en tiempo real")));
    }

    public AiFeatureResponse voiceAssistant(AiContextRequest req) {
        String input = nullSafe(req.getInput(), "Consulta general");
        return buildResponse("voice_assistant", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un asistente de voz adaptado para personas con discapacidad. " +
                        "Responde de forma breve, clara y fácil de leer en voz alta. " +
                        "Usa frases cortas y evita jerga técnica.",
                        input + "\nContexto: " + formatContext(req.getContext())));
    }

    public AiFeatureResponse metricsDashboard(AiContextRequest req) {
        return buildResponse("metrics_dashboard", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un analista de datos deportivos. Interpreta métricas avanzadas " +
                        "(asistencia, progreso, rendimiento, participación) y genera un resumen ejecutivo.",
                        buildContextPrompt(req, "métricas del usuario")));
    }

    public AiFeatureResponse historyComparison(AiContextRequest req) {
        List<BiomechanicalAnalysis> history = biomechanicalRepository
                .findByUsuarioIdOrderByCreatedAtDesc(req.getUsuarioId());

        String historyData = history.isEmpty() ? "Sin historial previo" :
                history.stream().limit(5)
                        .map(h -> String.format("Fecha: %s, Puntaje: %.1f, Rango: %d, Simetría: %d, Estabilidad: %d",
                                h.getCreatedAt(), h.getPuntaje(), h.getRangoMovimiento(),
                                h.getSimetria(), h.getEstabilidad()))
                        .collect(Collectors.joining("\n"));

        String prompt = "Historial biomecánico:\n" + historyData + "\n\nDatos actuales:\n"
                + formatContext(req.getContext()) + "\n\nCompara evolución, tendencias y recomendaciones.";

        return buildResponse("history_comparison", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un analista de progreso deportivo adaptado. Compara datos históricos con actuales.",
                        prompt));
    }

    public AiFeatureResponse eventRecommendations(AiContextRequest req) {
        return buildResponse("event_recommendations", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un recomendador de eventos deportivos inclusivos. " +
                        "Sugiere eventos según perfil, ubicación, discapacidad y preferencias.",
                        buildContextPrompt(req, "perfil y eventos disponibles")));
    }

    public AiFeatureResponse profileRecommendations(AiContextRequest req) {
        return buildResponse("profile_recommendations", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un asesor deportivo inclusivo. Recomienda actividades, deportes y recursos " +
                        "personalizados según el perfil completo del usuario.",
                        buildContextPrompt(req, "perfil del usuario")));
    }

    public AiFeatureResponse sportFiltering(AiContextRequest req) {
        return buildResponse("sport_filtering", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un experto en deportes adaptados. Filtra y clasifica deportes según " +
                        "tipo de discapacidad, nivel de habilidad, preferencias y restricciones médicas.",
                        buildContextPrompt(req, "criterios de filtrado y catálogo de deportes")));
    }

    public AiFeatureResponse disabilityDetection(AiContextRequest req) {
        return buildResponse("disability_detection", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un asistente de evaluación inicial para deportes adaptados. " +
                        "Basándote en la descripción del usuario, sugiere posibles categorías de discapacidad " +
                        "para orientar la selección de deportes. SIEMPRE recomienda evaluación profesional. " +
                        "No diagnostiques, solo orienta.",
                        buildContextPrompt(req, "descripción del usuario")));
    }

    public AiFeatureResponse competitionMode(AiContextRequest req) {
        return buildResponse("competition_mode", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un coach de competición en deportes adaptados. Proporciona estrategias, " +
                        "preparación mental, tácticas y plan de competición personalizado.",
                        buildContextPrompt(req, "datos de la competición")));
    }

    public AiFeatureResponse wearablesIntegration(AiContextRequest req) {
        return buildResponse("wearables_integration", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un analista de datos de wearables (smartwatch, sensores IMU, pulsómetros). " +
                        "Interpreta datos de dispositivos wearables y genera insights accionables " +
                        "para entrenamiento adaptado.",
                        buildContextPrompt(req, "datos de wearables")));
    }

    public AiFeatureResponse trainerAlerts(AiContextRequest req) {
        return buildResponse("trainer_alerts", req.getUsuarioId(),
                geminiService.generate(
                        "Eres un sistema de alertas inteligentes para entrenadores de deportes adaptados. " +
                        "Analiza datos de atletas y genera alertas prioritizadas (riesgo lesión, fatiga, " +
                        "baja asistencia, progreso estancado). Formato: ALERTA [PRIORIDAD]: descripción + acción.",
                        buildContextPrompt(req, "datos de atletas bajo supervisión")));
    }

    private AiFeatureResponse buildResponse(String feature, String userId, String result) {
        return buildResponse(feature, userId, result, Map.of());
    }

    private AiFeatureResponse buildResponse(String feature, String userId, String result,
                                              Map<String, Object> metadata) {
        return AiFeatureResponse.builder()
                .feature(feature)
                .userId(userId)
                .result(result)
                .metadata(metadata)
                .timestamp(LocalDateTime.now())
                .build();
    }

    private String buildContextPrompt(AiContextRequest req, String label) {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario: ").append(req.getUsuarioId()).append("\n");
        if (req.getInput() != null) {
            sb.append("Entrada: ").append(req.getInput()).append("\n");
        }
        sb.append(label).append(": ").append(formatContext(req.getContext()));
        return sb.toString();
    }

    private String formatContext(Map<String, Object> context) {
        if (context == null || context.isEmpty()) {
            return "No especificado";
        }
        return context.entrySet().stream()
                .map(e -> e.getKey() + ": " + e.getValue())
                .collect(Collectors.joining(", "));
    }

    private String nullSafe(String value, String defaultValue) {
        return value != null && !value.isBlank() ? value : defaultValue;
    }

    private String joinList(List<String> list) {
        if (list == null || list.isEmpty()) return "Ninguna";
        return String.join(", ", list);
    }

    private List<String> extractRecommendations(String aiAnalysis) {
        List<String> recs = new ArrayList<>();
        for (String line : aiAnalysis.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.matches("^(\\d+[.)\\-]|[-•*]).+")) {
                recs.add(trimmed.replaceFirst("^(\\d+[.)\\-]|[-•*])\\s*", ""));
            }
        }
        if (recs.isEmpty()) {
            recs.add("Consultar análisis completo en el campo result");
        }
        return recs.stream().limit(5).toList();
    }
}
