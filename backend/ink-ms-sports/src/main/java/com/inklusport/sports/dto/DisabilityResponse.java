package com.inklusport.sports.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class DisabilityResponse {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private Boolean isActive;
}