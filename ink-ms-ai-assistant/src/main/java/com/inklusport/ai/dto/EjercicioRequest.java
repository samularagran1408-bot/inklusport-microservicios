package com.inklusport.ai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EjercicioRequest {

    private String ejercicioId;

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder los 100 caracteres")
    private String nombre;

    @NotNull(message = "Las repeticiones son obligatorias")
    @Min(value = 0, message = "Las repeticiones no pueden ser negativas")
    private Integer repeticiones;

    @NotNull(message = "Las series son obligatorias")
    @Min(value = 0, message = "Las series no pueden ser negativas")
    private Integer series;

    @NotNull(message = "El tiempo estimado es obligatorio")
    @Min(value = 0, message = "El tiempo estimado no puede ser negativo")
    private Integer tiempoEstimado;

    private Map<String, Boolean> adaptaciones;

    @Min(value = 1, message = "El esfuerzo objetivo debe ser entre 1 y 5")
    @Max(value = 5, message = "El esfuerzo objetivo debe ser entre 1 y 5")
    private Integer esfuerzoObjetivo;

    @Min(value = 0, message = "El descanso no puede ser negativo")
    private Integer descansoSegundos;
}