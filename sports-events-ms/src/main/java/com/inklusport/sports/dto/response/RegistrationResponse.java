package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegistrationResponse {

    private String id;
    private String eventId;
    private String userId;
    private String status;
    private LocalDateTime registeredAt;
    private boolean onWaitlist;
    private Integer waitlistPosition;
}
