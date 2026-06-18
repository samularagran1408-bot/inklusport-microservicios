package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "planes_entrenamiento")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlan {

    @Id
    private String id;

    @Field("usuario_id")
    private String usuarioId;

    @Field("entrenador_id")
    private String entrenadorId;

    @Field("disability_type")
    private String disabilityType;

    private String nombre;
    private String descripcion;

    @Field("fecha_inicio")
    private LocalDateTime fechaInicio;

    @Field("fecha_fin")
    private LocalDateTime fechaFin;

    private List<Ejercicio> ejercicios;
    private String objetivo;

    @Field("sesiones_registradas")
    private List<SesionRegistrada> sesionesRegistradas;

    @Field("progreso_porcentaje")
    private Double progresoPorcentaje;

    private Boolean activo;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;
}