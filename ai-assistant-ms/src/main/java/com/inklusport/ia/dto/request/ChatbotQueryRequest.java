package com.inklusport.ia.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatbotQueryRequest {

    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;
}
