package com.inklusport.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BiomechanicalResponse {
    
    private String analysisId;
    
    private Double rangeOfMotion;
    
    private Double symmetry;
    
    private Double stability;
    
    private Double generalScore;
    
    private String fatigueLevel;
    
    private String recommendations;
}