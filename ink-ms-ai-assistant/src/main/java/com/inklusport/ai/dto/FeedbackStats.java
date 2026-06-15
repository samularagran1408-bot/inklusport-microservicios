package com.inklusport.ai.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedbackStats {
    private long totalFeedbacks;
    private long utilCount;
    private double porcentajeUtil;
}
