package com.inklusport.admin.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class DashboardStatsResponse {
    
    private Long totalAdmins;
    
    private Long pendingApprovals;
    
    private Long activeAlerts;
    
    private Long activeBlocks;
    
    private Map<String, Long> actionsByType;
    
    private Map<String, Long> alertsBySeverity;
}