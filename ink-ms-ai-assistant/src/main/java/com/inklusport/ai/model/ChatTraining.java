package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
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
    private String respuestaBase;
    private String intencion;
    private Map<String, String> respuestaAdaptada;
    private List<String> palabrasClave;
    private Integer prioridad;
    private Boolean activo;

    @CreatedDate
    private LocalDateTime fechaCreacion;
}