package com.inklusport.ai.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class TrainingPlanResponse {
    
    private String planId;
    
    private String name;
    
    private String description;
    
    private List<ExerciseResponse> exercises;
    
    private Integer totalWeeks;
}