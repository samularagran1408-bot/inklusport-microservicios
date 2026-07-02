package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReportResponse {
    
    private String id;
    
    private String name;
    
    private String type;
    
    private String scheduleCron;
    
    private Object parameters;
    
    private List<String> recipients;
    
    private LocalDateTime lastRun;
    
    private LocalDateTime nextRun;
    
    private Boolean isActive;
    
    private String createdBy;
    
    private LocalDateTime createdAt;
}