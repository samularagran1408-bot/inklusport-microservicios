package com.inklusport.admin.entity;

import com.inklusport.admin.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "scheduled_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledReport {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(name = "schedule_cron", nullable = false, length = 50)
    private String scheduleCron;

    @JdbcTypeCode(SqlTypes.JSON)
    private String parameters;

    @JdbcTypeCode(SqlTypes.JSON)
    private String recipients;

    @Column(name = "last_run")
    private LocalDateTime lastRun;

    @Column(name = "next_run")
    private LocalDateTime nextRun;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_by", columnDefinition = "CHAR(36)")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (isActive == null) {
            isActive = true;
        }
    }
}