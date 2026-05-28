package com.inklusport.ia.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class PlanEntrenamientoRequest {

    @NotBlank(message = "El usuarioId es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El entrenadorId es obligatorio")
    private String entrenadorId;

    @NotEmpty(message = "Debe enviar al menos un ejercicio")
    @Valid
    private List<EjercicioAdaptadoRequest> ejercicios;

    @Data
    public static class EjercicioAdaptadoRequest {
        @NotBlank(message = "El nombre del ejercicio es obligatorio")
        private String nombreEjercicio;

        @NotEmpty(message = "Debe incluir al menos una adaptacion")
        private List<String> adaptaciones;
    }
}
