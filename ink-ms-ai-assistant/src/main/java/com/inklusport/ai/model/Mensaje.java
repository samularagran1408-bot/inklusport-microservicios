package com.inklusport.ai.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mensaje {

    @Field("mensaje_id")
    private String mensajeId;

    private String mensaje;
    private String remitente;
    private LocalDateTime fecha;
}