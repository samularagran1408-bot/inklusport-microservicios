package com.inklusport.ai.service;

import com.inklusport.ai.dto.request.AlertRequest;
import com.inklusport.ai.dto.response.AlertResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final HuggingFaceService huggingFaceService;

    public AlertResponse trigger(AlertRequest request) {
        log.info("Generando alerta para usuario {}", request.getUserId());

        String analysis = huggingFaceService.generate(
                "Eres un sistema de alertas inteligentes para entrenadores de deportes adaptados.",
                String.format("Genera una alerta para el usuario %s. Tipo: %s. Contexto: %s. " +
                                "Formato: mensaje claro + severidad (info/warning/critical).",
                        request.getUserId(),
                        request.getType() != null ? request.getType() : "general",
                        request.getMessage() != null ? request.getMessage() : "Sin contexto adicional"));

        return AlertResponse.builder()
                .message(analysis)
                .severity("warning")
                .build();
    }
}
