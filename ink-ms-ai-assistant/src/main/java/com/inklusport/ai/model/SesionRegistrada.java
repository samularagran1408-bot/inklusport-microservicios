package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SesionRegistrada {

    private LocalDateTime fecha;

    @Field("ejercicios_completados")
    private List<Map<String, Object>> ejerciciosCompletados;

    @Field("repeticiones_realizadas")
    private Integer repeticionesRealizadas;

    @Field("esfuerzo_percibido")
    private Integer esfuerzoPercibido;

    @Field("dolor_reportado")
    private Boolean dolorReportado;

    private String notas;
    private Boolean completado;
}