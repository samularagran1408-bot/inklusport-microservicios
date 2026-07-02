package com.inklusport.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {

    @NotBlank(message = "El ID del mensaje es obligatorio")
    private String mensajeId;

    @NotNull(message = "El campo 'útil' es obligatorio")
    private Boolean util;

    private String comentario;
    private String usuarioId;
}