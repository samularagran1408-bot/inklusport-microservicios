package com.inklusport.ai.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    private String mensajeId;
    private String mensaje;
    private String remitente; /** usuario, asistente */
    private String intencion;
    private Map<String, Object> entidades;
    private Map<String, Object> respuestaAdaptada;
    private LocalDateTime fecha;
}