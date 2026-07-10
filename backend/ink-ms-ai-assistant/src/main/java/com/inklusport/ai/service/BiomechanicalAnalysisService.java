package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.BiomechanicalRequest;
import com.inklusport.ai.dto.response.BiomechanicalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BiomechanicalAnalysisService {

    private final HuggingFaceService huggingFaceService;

    public BiomechanicalResponse analyze(BiomechanicalRequest request) {
        log.info("Recibido análisis biomecánico para usuario {}", request.getUserId());

        String prompt = String.format("""
                Analiza el movimiento biomecánico de un deportista adaptado:
                - Usuario: %s
                - Tipo de movimiento: %s
                - Video URL: %s

                Responde en español con: 1) resumen breve, 2) nivel de riesgo (low/medium/high),
                3) recomendación principal. Formato conciso.
                """,
                request.getUserId(),
                request.getMovementType() != null ? request.getMovementType() : "No especificado",
                request.getVideoUrl() != null ? request.getVideoUrl() : "No proporcionado");

        String analysis = huggingFaceService.generate(
                "Eres un especialista en biomecánica deportiva adaptada.",
                prompt);

        return BiomechanicalResponse.builder()
                .summary(analysis)
                .riskLevel("medium")
                .recommendation("Ver análisis completo en el campo summary")
                .build();
    }
}
