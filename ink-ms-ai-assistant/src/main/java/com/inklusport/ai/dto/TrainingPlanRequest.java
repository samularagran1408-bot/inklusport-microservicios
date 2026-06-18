package com.inklusport.ai.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanRequest {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    private String entrenadorId;

    @Pattern(regexp = "^(visual|motriz|cognitiva|auditiva|multiple|no_especificado)$", 
             message = "Tipo de discapacidad no válido")
    private String disabilityType = "no_especificado";

    @NotBlank(message = "El nombre del plan es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    @NotEmpty(message = "Debe tener al menos un ejercicio")
    @Valid
    private List<EjercicioRequest> ejercicios;

    @Size(max = 200, message = "El objetivo no puede exceder los 200 caracteres")
    private String objetivo;

    private Boolean activo = true;
}