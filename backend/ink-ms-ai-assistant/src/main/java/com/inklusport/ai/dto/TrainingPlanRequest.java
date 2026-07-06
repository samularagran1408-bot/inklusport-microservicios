package com.inklusport.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanRequest {

    @NotBlank(message = "usuarioId es obligatorio")
    private String usuarioId;

    private String entrenadorId;
    private String tipoDiscapacidad;
    private String objetivo;
    private Integer semanasDuracion;
    private List<String> deportesPreferidos;
    private List<String> restricciones;
}
