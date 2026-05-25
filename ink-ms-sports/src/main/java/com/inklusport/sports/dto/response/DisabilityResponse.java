package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DisabilityResponse {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Boolean isActive;
    private List<Long> sportIds;
}