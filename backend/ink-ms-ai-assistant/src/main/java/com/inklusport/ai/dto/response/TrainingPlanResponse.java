package com.inklusport.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingPlanResponse {
    private String planName;
    private List<String> focusAreas;
    private int estimatedDurationMinutes;
}
