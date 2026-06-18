package com.inklusport.ai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String sessionId;
    private String response;
    private String intencion;
    private String disabilityType;
    private Boolean isAdapted;
    private LocalDateTime timestamp;

    /**
     * Información de seguimiento (si se necesita)
     */
    private Boolean needsMoreInfo;
    private String followUpQuestion;

    /**
     * Metadatos adicionales
     */
    private Map<String, Object> metadata;
    private List<String> sugerencias;
    private Double confidence; /** Confianza de la respuesta (0-1) */
}