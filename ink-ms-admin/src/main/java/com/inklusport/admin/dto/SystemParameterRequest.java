package com.inklusport.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SystemParameterRequest {

    @NotBlank(message = "La clave del parámetro es obligatoria")
    private String paramKey;
    
    @NotBlank(message = "El valor es obligatorio")
    private String paramValue;
    
    private String paramType;
    
    private String description;
}