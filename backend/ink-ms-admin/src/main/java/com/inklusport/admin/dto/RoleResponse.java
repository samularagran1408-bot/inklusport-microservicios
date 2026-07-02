package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class RoleResponse {
    private Integer id;

    private String name;

    private String description;

    private LocalDateTime createdAt;

    private List<String> permissions;
    
    private Integer adminCount;
}