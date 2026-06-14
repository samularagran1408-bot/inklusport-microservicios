package com.inklusport.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {
    
    @NotBlank(message = "El mensaje es obligatorio")
    private String message;
    
    private String sessionId;
}