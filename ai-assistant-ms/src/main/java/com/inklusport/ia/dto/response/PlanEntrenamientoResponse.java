package com.inklusport.ia.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class PlanEntrenamientoResponse {
    String id;
    String usuarioId;
    String entrenadorId;
    List<EjercicioAdaptadoResponse> ejercicios;
    Instant updatedAt;

    @Value
    @Builder
    public static class EjercicioAdaptadoResponse {
        String nombreEjercicio;
        List<String> adaptaciones;
    }
}
