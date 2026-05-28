package com.inklusport.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovalRequest {
    @NotBlank(message = "El tipo de entidad es obligatorio")
    private String targetType;
    
    @NotNull(message = "Los datos son obligatorios")
    private Object targetData;
}