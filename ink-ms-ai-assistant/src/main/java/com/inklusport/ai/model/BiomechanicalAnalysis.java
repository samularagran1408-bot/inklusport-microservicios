package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "analisis_biomecanico")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiomechanicalAnalysis {

    @Id
    private String id;

    @Indexed
    private String usuarioId;

    private String planId;
    private String disabilityType;
    private LocalDateTime fecha;
    private String sessionId;
    private String ejercicioNombre;

    private Double rangoMovimiento;
    private Double simetria;
    private Double estabilidad;
    private Double puntajeGeneral;

    private String recomendaciones;

    @CreatedDate
    private LocalDateTime createdAt;
}