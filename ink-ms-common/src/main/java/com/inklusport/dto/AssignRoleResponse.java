package com.inklusport.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignRoleResponse {
    private String email;
    private Long roleId;
    private String roleName;
    private String message;
}
