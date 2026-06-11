package com.inklusport.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRoleRequest {
    @NotBlank(message = "El ID del administrador es obligatorio")
    private String adminId;
    
    @NotNull(message = "El ID del rol es obligatorio")
    private Integer roleId;

    private String assignedBy;
}