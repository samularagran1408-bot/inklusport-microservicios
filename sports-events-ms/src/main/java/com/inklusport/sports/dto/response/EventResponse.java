package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventResponse {

    private String id;
    private String sportId;
    private String sportName;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxParticipants;
    private Long registeredCount;
    private String status;
    private LocalDateTime createdAt;
}
