package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SportResponse {

    private String id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<String> disabilityIds;
}
