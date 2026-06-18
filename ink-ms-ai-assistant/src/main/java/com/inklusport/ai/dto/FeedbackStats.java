package com.inklusport.ai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackStats {

    private Long totalFeedback;
    private Long usefulCount;
    private Long notUsefulCount;
    private Double usefulPercentage;

    /**
     * Estadísticas por período
     */
    private Map<String, Long> feedbackByDay;
    private Map<String, Long> feedbackByHour;
    private Map<String, Long> feedbackByIntencion;

    /**
     * Top usuarios
     */
    private Map<String, Long> topUsersByFeedback;
    private Map<String, Long> topUsersByUseful;

    /**
     * Métricas de calidad
     */
    private Double averageRating;
    private Double satisfactionScore;

    /**
     * Última actualización
     */
    private LocalDateTime lastUpdated;
    private String period; /** DAILY, WEEKLY, MONTHLY */
}