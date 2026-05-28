package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AlertResponse {
    private String id;
    
    private String type;
    
    private String severity;
    
    private String title;
    
    private String description;
    
    private String targetId;
    
    private String targetType;
    
    private Boolean resolved;
    
    private String resolvedBy;
    
    private String resolvedByName;
    
    private LocalDateTime resolvedAt;
    
    private LocalDateTime createdAt;
}