package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.FatigueRequest;
import com.inklusport.ai.dto.response.FatigueResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FatigueDetectionService {

    private final HuggingFaceService huggingFaceService;

    public FatigueResponse evaluate(FatigueRequest request) {
        log.info("Analizando fatiga para usuario {}", request.getUserId());

        String prompt = String.format("""
                Evalúa el nivel de fatiga de un deportista adaptado:
                - Usuario: %s
                - Horas de sueño: %.1f
                - Nivel de estrés: %.1f (0-10)

                Responde en español indicando: estado (stable/moderate/high), puntuación de fatiga (0.0-1.0)
                y mensaje de recomendación. Sé conciso.
                """,
                request.getUserId(), request.getSleepHours(), request.getStressLevel());

        String analysis = huggingFaceService.generate(
                "Eres un monitor de rendimiento deportivo especializado en deportes adaptados.",
                prompt);

        return FatigueResponse.builder()
                .status("stable")
                .fatigueScore(0.35)
                .message(analysis)
                .build();
    }
}
