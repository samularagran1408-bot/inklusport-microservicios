package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ejercicio {

    private String ejercicioId;
    private String nombre;
    private Integer repeticiones;
    private Integer series;
    private Integer tiempoEstimado;
    private Map<String, Boolean> adaptaciones;
    private Integer esfuerzoObjetivo;
    private Integer descansoSegundos;
}