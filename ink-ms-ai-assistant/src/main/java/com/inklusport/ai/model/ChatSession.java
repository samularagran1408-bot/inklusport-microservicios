package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "conversaciones_chatbot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSession {

    @Id
    private String id;

    @Indexed
    private String usuarioId;

    private String disabilityType;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;

    private List<Mensaje> mensajes;
    private String resumen;
    private LocalDateTime ultimaInteraccion;

    @CreatedDate
    private LocalDateTime createdAt;
}