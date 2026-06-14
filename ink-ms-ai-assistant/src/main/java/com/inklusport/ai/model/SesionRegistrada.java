package com.inklusport.ai.model;

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
public class SesionRegistrada {

    private LocalDateTime fecha;
    private List<Object> ejerciciosCompletados;
    private Integer repeticionesRealizadas;
    private Integer esfuerzoPercibido;
    private Boolean dolorReportado;
    private String notas;
    private Boolean completado;
}