package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminAuditResponse {
    private String id;

    private String adminId;

    private String adminEmail;

    private String adminName;

    private String action;

    private String targetType;

    private String targetId;

    private Object details;

    private String ipAddress;
    
    private LocalDateTime createdAt;
}