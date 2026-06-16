package com.inklusport.ai.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainingPlanRequest {
    
    @NotBlank(message = "El deporte es obligatorio")
    private String sport;
    
    
    private String disabilityType;
    
    private String level;
    
    private Integer durationWeeks;
}