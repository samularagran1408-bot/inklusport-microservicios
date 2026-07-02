package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AiConfigResponse {
    
    private Integer id;
    
    private String featureName;
    
    private Boolean isEnabled;
    
    private String modelVersion;
    
    private BigDecimal confidenceThreshold;
    
    private Object parameters;
    
    private String updatedBy;
    
    private LocalDateTime updatedAt;
}