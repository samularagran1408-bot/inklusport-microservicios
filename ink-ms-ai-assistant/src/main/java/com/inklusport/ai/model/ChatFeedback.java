package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "feedback_chatbot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatFeedback {

    @Id
    private String id;

    private String conversacionId;
    private String usuarioId;
    private String mensajeId;
    private Boolean util;
    private String comentario;
    private LocalDateTime fecha;
}