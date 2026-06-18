package com.inklusport.ai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanResponse {

    private String id;
    private String usuarioId;
    private String entrenadorId;
    private String disabilityType;
    private String nombre;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private List<ExerciseResponse> ejercicios;
    private String objetivo;
    private Double progresoPorcentaje;
    private Boolean activo;

    /**
     * Estadísticas del plan
     */
    private Integer totalSesiones;
    private Integer sesionesCompletadas;
    private Double nivelCumplimiento;
    private Double esfuerzoPromedio;

    /**
     * Estados
     */
    private String estado; /** ACTIVO, COMPLETADO, VENCIDO, PAUSADO */
    private Integer diasRestantes;
    private Boolean estaVencido;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}