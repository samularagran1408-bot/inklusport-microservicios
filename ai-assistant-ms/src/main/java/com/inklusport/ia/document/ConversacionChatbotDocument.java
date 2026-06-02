package com.inklusport.ia.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;

@Document(collection = "conversaciones_chatbot")
@Data
public class ConversacionChatbotDocument {

    @Id
    private String id;

    @Indexed
    @Field("usuario_id")
    private String usuarioId;

    @Field("ultimo_mensaje_usuario")
    private String ultimoMensajeUsuario;

    @Field("intencion_detectada")
    private String intencionDetectada;

    @Field("respuesta_bot")
    private String respuestaBot;

    @Field("estado_conversacion")
    private String estadoConversacion;

    @Field("updated_at")
    private Instant updatedAt;
}
