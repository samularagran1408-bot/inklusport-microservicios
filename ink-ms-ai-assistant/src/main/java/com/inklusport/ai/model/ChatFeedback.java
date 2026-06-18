package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "feedback_chatbot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatFeedback {

    @Id
    private String id;

    @Field("conversacion_id")
    private String conversacionId;

    @Field("usuario_id")
    private String usuarioId;

    @Field("mensaje_id")
    private String mensajeId;

    private Boolean util;
    private String comentario;
    private LocalDateTime fecha;
}