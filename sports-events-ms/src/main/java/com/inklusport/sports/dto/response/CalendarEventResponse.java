package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CalendarEventResponse {

    private String id;
    private String title;
    private String sportName;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
}
