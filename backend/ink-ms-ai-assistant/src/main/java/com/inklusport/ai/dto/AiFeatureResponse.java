package com.inklusport.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiFeatureResponse {
    private String feature;
    private String userId;
    private String result;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
}
