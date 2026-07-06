package com.inklusport.ai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiomechanicalAnalysisRequest {

    @NotBlank(message = "usuarioId es obligatorio")
    private String usuarioId;

    private String tipoDiscapacidad;

    @NotNull(message = "rangoMovimiento es obligatorio")
    @Min(0) @Max(100)
    private Integer rangoMovimiento;

    @NotNull(message = "simetria es obligatoria")
    @Min(0) @Max(100)
    private Integer simetria;

    @NotNull(message = "estabilidad es obligatoria")
    @Min(0) @Max(100)
    private Integer estabilidad;

    private String deportePracticado;
    private String notasAdicionales;
}
