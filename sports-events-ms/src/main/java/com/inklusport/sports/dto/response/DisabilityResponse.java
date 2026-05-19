package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DisabilityResponse {

    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
}
