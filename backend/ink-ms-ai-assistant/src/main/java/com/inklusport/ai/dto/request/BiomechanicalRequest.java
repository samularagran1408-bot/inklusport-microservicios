package com.inklusport.ai.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BiomechanicalRequest {
    @NotBlank
    private String userId;
    private String movementType;
    private String videoUrl;
}
