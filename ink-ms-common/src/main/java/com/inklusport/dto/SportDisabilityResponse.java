package com.inklusport.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SportDisabilityResponse {
    private Integer sportId;
    private String sportName;
    private Integer disabilityId;
    private String disabilityName;
    private String adaptations;
}