package com.inklusport.ai.service;

import com.inklusport.ai.model.DisabilityProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisabilityDetectionService {

    private final HuggingFaceService huggingFaceService;

    public DisabilityProfile detect(String userId) {
        log.info("Detectando perfil de discapacidad para usuario {}", userId);

        String notes = huggingFaceService.generate(
                "Eres un asistente de evaluación inicial para deportes adaptados. " +
                "No diagnostiques, solo orienta. SIEMPRE recomienda evaluación profesional.",
                "Orienta sobre categorías de discapacidad deportiva para el usuario " + userId
                        + " y deportes adaptados recomendados.");

        return DisabilityProfile.builder()
                .userId(userId)
                .needsSupport(false)
                .notes(notes)
                .build();
    }
}
