package com.inklusport.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackRequest {

    @NotBlank(message = "El ID de conversación es obligatorio")
    private String conversacionId;

    @NotBlank(message = "El ID del mensaje es obligatorio")
    private String mensajeId;

    @NotNull(message = "Indique si la respuesta fue útil")
    private Boolean util;

    private String comentario;
}