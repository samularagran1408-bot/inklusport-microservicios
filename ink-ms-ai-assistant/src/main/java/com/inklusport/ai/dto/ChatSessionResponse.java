package com.inklusport.ai.dto;

import com.inklusport.ai.model.Mensaje;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatSessionResponse {

    private String id;
    private String usuarioId;
    private String disabilityType;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String estado;
    private List<Mensaje> mensajes;
    private String resumen;
    private LocalDateTime ultimaInteraccion;

    /**
     * Estadísticas
     */
    private Integer totalMensajes;
    private Integer mensajesUsuario;
    private Integer mensajesAsistente;
    private Double duracionMinutos;

    /** 
     * Último mensaje
     */
    private String ultimoMensaje;
    private String ultimoRemitente;
    private LocalDateTime ultimoMensajeFecha;

    private LocalDateTime createdAt;
}