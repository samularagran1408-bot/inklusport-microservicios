package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.TrainingPlanRequest;
import com.inklusport.ai.dto.response.TrainingPlanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingPlanService {

    private final HuggingFaceService huggingFaceService;

    public TrainingPlanResponse buildPlan(TrainingPlanRequest request) {
        log.info("Solicitando plan de entrenamiento para usuario {}", request.getUserId());

        String prompt = String.format("""
                Crea un plan de entrenamiento personalizado:
                - Usuario: %s
                - Objetivo: %s
                - Tipo de discapacidad: %s

                Incluye nombre del plan, áreas de enfoque y duración estimada en minutos por sesión.
                Responde en español de forma estructurada.
                """,
                request.getUserId(),
                request.getGoal() != null ? request.getGoal() : "Mejora general",
                request.getDisabilityType() != null ? request.getDisabilityType() : "No especificada");

        String plan = huggingFaceService.generate(
                "Eres un entrenador certificado en deportes adaptados e inclusivos.",
                prompt);

        return TrainingPlanResponse.builder()
                .planName("Plan personalizado IA")
                .focusAreas(List.of("resistencia", "movilidad", "fuerza"))
                .estimatedDurationMinutes(45)
                .build();
    }
}
