package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PendingApprovalResponse {
    private String id;
    
    private String targetType;
    
    private Object targetData;
    
    private String requestedBy;
    
    private String requestedByName;
    
    private LocalDateTime requestedAt;
    
    private String status;
    
    private String reviewedBy;
    
    private String reviewedByName;
    
    private LocalDateTime reviewedAt;
    
    private String reviewNotes;
}