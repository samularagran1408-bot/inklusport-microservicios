package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SystemConfigResponse {
    
    private String configKey;
    
    private String configValue;
    
    private String description;
    
    private String updatedBy;
    
    private LocalDateTime updatedAt;
}