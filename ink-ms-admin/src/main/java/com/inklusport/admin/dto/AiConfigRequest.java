package com.inklusport.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AiConfigRequest {
    @NotBlank(message = "El nombre de la funcionalidad es obligatorio")
    private String featureName;
    
    private Boolean isEnabled;

    private String modelVersion;

    private BigDecimal confidenceThreshold;
    
    private Object parameters;
}