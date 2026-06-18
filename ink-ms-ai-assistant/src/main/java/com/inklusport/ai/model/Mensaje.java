package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    @Field("mensaje_id")
    @Builder.Default
    private String mensajeId = java.util.UUID.randomUUID().toString();

    private String mensaje;
    private String remitente;
    private String intencion;
    private Map<String, Object> entidades;

    @Field("respuesta_adaptada")
    private Map<String, Object> respuestaAdaptada;

    private LocalDateTime fecha;
}