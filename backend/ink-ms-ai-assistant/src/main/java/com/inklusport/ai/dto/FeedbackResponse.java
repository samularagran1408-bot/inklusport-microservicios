package com.inklusport.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {

    private String id;
    private String mensajeId;
    private Boolean util;
    private String comentario;
    private LocalDateTime fecha;
}