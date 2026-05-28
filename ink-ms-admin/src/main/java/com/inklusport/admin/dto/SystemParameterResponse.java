package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SystemParameterResponse {
    private Integer id;

    private String paramKey;
    
    private String paramValue;
    
    private String paramType;
    
    private String description;
    
    private String updatedBy;
    
    private LocalDateTime updatedAt;
}