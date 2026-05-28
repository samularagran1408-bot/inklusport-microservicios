package com.inklusport.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminActionRequest {
    @NotBlank(message = "La acción es obligatoria")
    private String action;
    
    private String targetType;

    private String targetId;

    private Object details;
    
    private String ipAddress;
}