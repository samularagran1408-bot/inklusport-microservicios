package com.inklusport.ai.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiomechanicalRequest {
    
    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    private String planId;

    @Pattern(regexp = "^(visual|motriz|cognitiva|auditiva|multiple|no_especificado)$", 
             message = "Tipo de discapacidad no válido")
    private String disabilityType = "no_especificado";

    private String sessionId;

    private String ejercicioNombre;

    @NotNull(message = "El rango de movimiento es obligatorio")
    @Min(value = 0, message = "El rango de movimiento debe ser entre 0 y 100")
    @Max(value = 100, message = "El rango de movimiento debe ser entre 0 y 100")
    private Double rangoMovimiento;

    @NotNull(message = "La simetría es obligatoria")
    @Min(value = 0, message = "La simetría debe ser entre 0 y 100")
    @Max(value = 100, message = "La simetría debe ser entre 0 y 100")
    private Double simetria;

    @NotNull(message = "La estabilidad es obligatoria")
    @Min(value = 0, message = "La estabilidad debe ser entre 0 y 100")
    @Max(value = 100, message = "La estabilidad debe ser entre 0 y 100")
    private Double estabilidad;

    private String recomendaciones;
}