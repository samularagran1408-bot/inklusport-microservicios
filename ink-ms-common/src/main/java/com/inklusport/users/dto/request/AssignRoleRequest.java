package com.inklusport.users.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRoleRequest {
    
    @NotNull(message = "El ID del rol es obligatorio")
    private Long roleId;
}
