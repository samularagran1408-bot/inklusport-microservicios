package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "conversaciones_chatbot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSession {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("disability_type")
    private String disabilityType;

    @Field("fecha_inicio")
    private LocalDateTime fechaInicio;

    @Field("fecha_fin")
    private LocalDateTime fechaFin;

    private String estado;

    @Builder.Default
    private List<Mensaje> mensajes = new ArrayList<>();

    private String resumen;

    @Field("ultima_interaccion")
    private LocalDateTime ultimaInteraccion;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}