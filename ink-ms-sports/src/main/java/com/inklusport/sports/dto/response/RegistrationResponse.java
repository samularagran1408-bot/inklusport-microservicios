package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class RegistrationResponse {
    private String id;
    private String userId;
    private String eventId;
    private String eventName;
    private LocalDateTime registrationDate;
    private Boolean attended;
    private Integer waitlistPosition;
    private String qrCode;
}