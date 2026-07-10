package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.InjuryPredictionRequest;
import com.inklusport.ai.dto.response.InjuryPredictionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InjuryPredictionService {

    private final HuggingFaceService huggingFaceService;

    public InjuryPredictionResponse predict(InjuryPredictionRequest request) {
        log.info("Evaluando riesgo de lesión para usuario {}", request.getUserId());

        String prompt = String.format("""
                Evalúa el riesgo de lesión de un deportista adaptado:
                - Usuario: %s
                - Actividad: %s
                - Carga de entrenamiento: %.1f

                Responde en español con: nivel de riesgo (low/medium/high), recomendación preventiva
                y factores de riesgo identificados.
                """,
                request.getUserId(),
                request.getActivity() != null ? request.getActivity() : "No especificada",
                request.getLoad());

        String analysis = huggingFaceService.generate(
                "Eres un fisioterapeuta deportivo especializado en prevención de lesiones en deportes adaptados.",
                prompt);

        return InjuryPredictionResponse.builder()
                .riskLevel("low")
                .confidence(0.72)
                .recommendation(analysis)
                .build();
    }
}
