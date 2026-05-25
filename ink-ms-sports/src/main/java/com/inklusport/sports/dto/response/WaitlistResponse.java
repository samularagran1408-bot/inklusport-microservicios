package com.inklusport.sports.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class WaitlistResponse {
    private String id;
    private String userId;
    private String eventId;
    private String eventName;
    private Integer position;
    private String status;
    private LocalDateTime requestedAt;
    private Boolean notified;
    private LocalDateTime notifiedAt;
}