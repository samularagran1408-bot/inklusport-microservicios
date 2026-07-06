package com.inklusport.ai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiomechanicalResponse {
    private String summary;
    private String riskLevel;
    private String recommendation;
}
