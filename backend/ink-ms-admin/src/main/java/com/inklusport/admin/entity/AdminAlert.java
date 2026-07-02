package com.inklusport.admin.entity;

import com.inklusport.admin.enums.AlertSeverity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_alert")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminAlert {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false, length = 50)
    private String type;

    @Enumerated(EnumType.STRING)
    private AlertSeverity severity;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(name = "target_type", length = 50)
    private String targetType;

    private Boolean resolved;

    @Column(name = "resolved_by", columnDefinition = "CHAR(36)")
    private String resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (severity == null) {
            severity = AlertSeverity.medium;
        }
        if (resolved == null) {
            resolved = false;
        }
    }
}