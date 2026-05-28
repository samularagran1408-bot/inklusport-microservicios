package com.inklusport.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlertRequest {
    @NotBlank(message = "El tipo de alerta es obligatorio")
    private String type;
    
    @NotBlank(message = "La severidad es obligatoria")
    private String severity;
    
    @NotBlank(message = "El título es obligatorio")
    private String title;
    
    private String description;

    private String targetId;
    
    private String targetType;
}