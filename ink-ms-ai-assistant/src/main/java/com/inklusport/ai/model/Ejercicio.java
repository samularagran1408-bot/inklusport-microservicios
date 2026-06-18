package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ejercicio {

    @Field("ejercicio_id")
    private String ejercicioId;

    private String nombre;
    private Integer repeticiones;
    private Integer series;

    @Field("tiempo_estimado")
    private Integer tiempoEstimado;

    private Map<String, Boolean> adaptaciones;

    @Field("esfuerzo_objetivo")
    private Integer esfuerzoObjetivo;

    @Field("descanso_segundos")
    private Integer descansoSegundos;
}