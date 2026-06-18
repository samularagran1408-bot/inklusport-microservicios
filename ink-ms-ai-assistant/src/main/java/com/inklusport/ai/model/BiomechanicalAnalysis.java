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

@Document(collection = "analisis_biomecanico")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiomechanicalAnalysis {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("plan_id")
    private String planId;

    @Field("disability_type")
    private String disabilityType;

    private LocalDateTime fecha;

    @Field("session_id")
    private String sessionId;

    @Field("ejercicio_nombre")
    private String ejercicioNombre;

    @Field("rango_movimiento")
    private Double rangoMovimiento;

    private Double simetria;
    private Double estabilidad;

    @Field("puntaje_general")
    private Double puntajeGeneral;

    private String recomendaciones;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;
}