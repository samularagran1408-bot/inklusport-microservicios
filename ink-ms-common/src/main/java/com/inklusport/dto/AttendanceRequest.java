package com.inklusport.dto;

import lombok.Data;

@Data
public class AttendanceRequest {
    
    private String registrationId;
    
    private String checkInMethod;
    
    private String verifiedBy;
}