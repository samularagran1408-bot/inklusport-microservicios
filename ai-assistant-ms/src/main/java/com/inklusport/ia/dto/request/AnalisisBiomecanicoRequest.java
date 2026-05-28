package com.inklusport.ia.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnalisisBiomecanicoRequest {

    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El tipoDiscapacidad es obligatorio")
    private String tipoDiscapacidad;

    @NotNull(message = "El rangoMovimiento es obligatorio")
    @Min(value = 0, message = "El rangoMovimiento no puede ser menor a 0")
    @Max(value = 100, message = "El rangoMovimiento no puede ser mayor a 100")
    private Integer rangoMovimiento;

    @NotNull(message = "La simetria es obligatoria")
    @Min(value = 0, message = "La simetria no puede ser menor a 0")
    @Max(value = 100, message = "La simetria no puede ser mayor a 100")
    private Integer simetria;

    @NotNull(message = "La estabilidad es obligatoria")
    @Min(value = 0, message = "La estabilidad no puede ser menor a 0")
    @Max(value = 100, message = "La estabilidad no puede ser mayor a 100")
    private Integer estabilidad;
}
