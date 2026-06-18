package com.inklusport.ai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 1000, message = "El mensaje no puede exceder los 1000 caracteres")
    private String message;

    private String sessionId;

    @Pattern(regexp = "^(visual|motriz|cognitiva|auditiva|multiple|no_especificado)$", 
             message = "Tipo de discapacidad no válido")
    private String disabilityType = "no_especificado";

    private String userId;
}