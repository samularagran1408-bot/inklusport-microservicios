package com.inklusport.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ai_configuration")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "feature_name", nullable = false, unique = true, length = 100)
    private String featureName;

    @Column(name = "is_enabled")
    private Boolean isEnabled;

    @Column(name = "model_version", length = 50)
    private String modelVersion;

    @Column(name = "confidence_threshold", precision = 3, scale = 2)
    private BigDecimal confidenceThreshold;

    @JdbcTypeCode(SqlTypes.JSON)
    private String parameters;

    @Column(name = "updated_by", columnDefinition = "CHAR(36)")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (isEnabled == null) {
            isEnabled = true;
        }
        if (confidenceThreshold == null) {
            confidenceThreshold = new BigDecimal("0.75");
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}