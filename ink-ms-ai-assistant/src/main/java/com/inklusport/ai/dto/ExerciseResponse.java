package com.inklusport.ai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResponse {

    private String id;
    private String nombre;
    private Integer repeticiones;
    private Integer series;
    private Integer tiempoEstimado;
    private Integer esfuerzoObjetivo;
    private Integer descansoSegundos;
    private Map<String, Boolean> adaptaciones;

    // Información adicional
    private String nivelDificultad;  // PRINCIPIANTE, INTERMEDIO, AVANZADO
    private String grupoMuscular;
    private List<String> beneficios;
    private List<String> contraindicaciones;
    private String instrucciones;
}