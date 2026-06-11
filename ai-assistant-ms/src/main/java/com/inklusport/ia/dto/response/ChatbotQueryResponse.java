package com.inklusport.ia.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class ChatbotQueryResponse {
    String conversacionId;
    String usuarioId;
    String mensajeUsuario;
    String intencionDetectada;
    String respuestaBot;
    String estadoConversacion;
    Instant updatedAt;
}
