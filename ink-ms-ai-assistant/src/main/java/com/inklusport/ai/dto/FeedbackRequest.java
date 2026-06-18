package com.inklusport.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {

    @NotBlank(message = "El ID de la conversación es obligatorio")
    private String conversacionId;

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El ID del mensaje es obligatorio")
    private String mensajeId;

    @NotNull(message = "El campo 'útil' es obligatorio")
    private Boolean util;

    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    private String comentario;
}