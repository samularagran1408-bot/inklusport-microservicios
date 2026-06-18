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
public class BiomechanicalResponse {

    private String id;
    private String usuarioId;
    private String planId;
    private String disabilityType;
    private Double puntajeGeneral;
    private String recomendaciones;
    private List<String> recomendacionesDetalladas;
    private LocalDateTime timestamp;

    /**
     * Métricas adicionales
     */
    private Double rangoMovimiento;
    private Double simetria;
    private Double estabilidad;

    /**
     * Niveles de evaluación
     */
    private String nivelRangoMovimiento;  // BAJO, MEDIO, ALTO
    private String nivelSimetria;         // BAJO, MEDIO, ALTO
    private String nivelEstabilidad;      // BAJO, MEDIO, ALTO
    private String nivelGeneral;          // BAJO, MEDIO, ALTO
}