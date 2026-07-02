package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionResponse {
    private Integer id;

    private String name;

    private String resource;

    private String action;
    
    private String description;
}