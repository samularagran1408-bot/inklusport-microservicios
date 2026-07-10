package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.CompetitionRequest;
import com.inklusport.ai.dto.response.CompetitionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompetitionService {

    private final HuggingFaceService huggingFaceService;

    public CompetitionResponse prepare(CompetitionRequest request) {
        log.info("Preparando modo competencia para usuario {}", request.getUserId());

        huggingFaceService.generate(
                "Eres un coach de competición en deportes adaptados.",
                String.format("Prepara estrategia de competición para usuario %s. Evento: %s. Nivel: %s.",
                        request.getUserId(),
                        request.getEventType() != null ? request.getEventType() : "No especificado",
                        request.getLevel() != null ? request.getLevel() : "Intermedio"));

        return CompetitionResponse.builder()
                .mode("competition")
                .status("ready")
                .build();
    }
}
