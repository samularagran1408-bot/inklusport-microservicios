package com.inklusport.ia.dto.response;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Value
@Builder
public class AnalisisBiomecanicoResponse {
    String id;
    String usuarioId;
    String tipoDiscapacidad;
    Integer rangoMovimiento;
    Integer simetria;
    Integer estabilidad;
    Double puntaje;
    List<String> recomendaciones;
    Instant fechaAnalisis;
}
