package com.inklusport.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Field("mensaje_id")
    private String mensajeId;

    @Field("usuario_id")
    private String usuarioId;

    private Boolean util;
    private String comentario;
    private LocalDateTime fecha;
}