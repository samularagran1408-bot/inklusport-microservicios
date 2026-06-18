package com.inklusport.ai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private String id;
    private String conversacionId;
    private String usuarioId;
    private String mensajeId;
    private Boolean util;
    private String comentario;
    private LocalDateTime fecha;

    /**
     * Estadísticas adicionales
     */
    private Boolean processed;
    private String feedbackType; /** POSITIVO, NEGATIVO, NEUTRO */
    private LocalDateTime processedAt;
}