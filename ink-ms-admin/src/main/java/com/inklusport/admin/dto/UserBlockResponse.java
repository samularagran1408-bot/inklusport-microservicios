package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserBlockResponse {
    private String id;
    
    private String userId;
    
    private String userEmail;
    
    private String userName;
    
    private String blockedBy;
    
    private String blockedByName;
    
    private String reason;
    
    private String blockType;
    
    private LocalDateTime expiresAt;
    
    private Boolean isActive;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime unblockedAt;
}