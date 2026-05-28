package com.inklusport.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionRequest {
    @NotBlank(message = "El nombre del permiso es obligatorio")
    private String name;
    
    @NotBlank(message = "El recurso es obligatorio")
    private String resource;
    
    @NotBlank(message = "La acción es obligatoria")
    private String action;
    
    private String description;
}