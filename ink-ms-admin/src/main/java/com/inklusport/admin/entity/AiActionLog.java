package main.java.com.inklusport.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ai_action_log")
public class AiActionLog {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "ai_feature", nullable = false, length = 100)
    private String aiFeature;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "prediction_value", columnDefinition = "json")
    private Map<String, Object> predictionValue;

    @Column(precision = 3, scale = 2)
    private BigDecimal confidence;

    @Column(name = "recommended_action", columnDefinition = "TEXT")
    private String recommendedAction;

    @Column(name = "was_applied")
    @Builder.Default
    private Boolean wasApplied = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
    }
}