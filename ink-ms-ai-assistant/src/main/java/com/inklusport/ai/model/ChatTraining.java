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
import java.util.Map;

@Document(collection = "entrenamiento_chatbot")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatTraining {

    @Id
    private String id;

    private String pregunta;

    @Field("respuesta_base")
    private String respuestaBase;

    private String intencion;

    @Field("respuesta_adaptada")
    private Map<String, String> respuestaAdaptada;

    @Field("palabras_clave")
    @Builder.Default
    private List<String> palabrasClave = new ArrayList<>();

    private Integer prioridad;

    private Boolean activo;

    @Field("fecha_creacion")
    @CreatedDate
    private LocalDateTime fechaCreacion;
}