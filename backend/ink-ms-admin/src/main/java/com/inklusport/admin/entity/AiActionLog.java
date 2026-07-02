package com.inklusport.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_action_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiActionLog {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String userId;

    @Column(name = "ai_feature", nullable = false, length = 100)
    private String aiFeature;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prediction_value")
    private String predictionValue;

    @Column(precision = 3, scale = 2)
    private BigDecimal confidence;

    @Column(name = "recommended_action", columnDefinition = "TEXT")
    private String recommendedAction;

    @Column(name = "was_applied")
    private Boolean wasApplied;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (wasApplied == null) {
            wasApplied = false;
        }
    }
}