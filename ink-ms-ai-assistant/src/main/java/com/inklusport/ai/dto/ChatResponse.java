package com.inklusport.ai.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ChatResponse {

    private String sessionId;
    
    private String response;
    
    private LocalDateTime timestamp;
}