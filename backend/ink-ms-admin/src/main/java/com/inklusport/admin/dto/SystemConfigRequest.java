package com.inklusport.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SystemConfigRequest {
    @NotBlank(message = "La clave de configuración es obligatoria")
    private String configKey;
    
    @NotBlank(message = "El valor es obligatorio")
    private String configValue;
    
    private String description;
}