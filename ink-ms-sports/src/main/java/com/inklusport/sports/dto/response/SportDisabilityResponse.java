package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SportDisabilityResponse {
    private Long sportId;
    private String sportName;
    private Long disabilityId;
    private String disabilityName;
    private String adaptations;
}