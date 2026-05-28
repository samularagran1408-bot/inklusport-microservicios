package com.inklusport.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserBlockRequest {
    @NotBlank(message = "El ID del usuario es obligatorio")
    private String userId;
    
    @NotBlank(message = "El motivo es obligatorio")
    private String reason;
    
    @NotBlank(message = "El tipo de bloqueo es obligatorio")
    private String blockType;  /** temporal, permanente */
    
    private LocalDateTime expiresAt;
}