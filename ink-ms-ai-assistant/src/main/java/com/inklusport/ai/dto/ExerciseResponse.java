package com.inklusport.ai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseResponse {
    
    private String name;
    
    private Integer repetitions;
    
    private Integer sets;
    
    private Integer estimatedTime;
    
    private String adaptations;
}