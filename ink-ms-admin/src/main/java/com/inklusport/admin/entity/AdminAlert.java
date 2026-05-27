package main.java.com.inklusport.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_alert")
public class AdminAlert {

    public enum AlertSeverity {
        low, medium, high, critical
    }

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 50)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('low', 'medium', 'high', 'critical')")
    @Builder.Default
    private AlertSeverity severity = AlertSeverity.medium;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(name = "target_type", length = 50)
    private String targetType;

    @Builder.Default
    private Boolean resolved = false;

    @Column(name = "resolved_by", length = 36)
    private String resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

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