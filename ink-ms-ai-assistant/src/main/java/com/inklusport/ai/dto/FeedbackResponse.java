package com.inklusport.ai.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class FeedbackResponse {
    private String id;
    private String conversacionId;
    private String usuarioId;
    private String mensajeId;
    private Boolean util;
    private String comentario;
    private LocalDateTime fecha;
}