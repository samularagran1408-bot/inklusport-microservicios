package com.inklusport.admin.entity;

import com.inklusport.admin.enums.RequestStatus;
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
@Table(name = "pending_approval")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PendingApproval {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id;

    @Column(name = "target_type", nullable = false, length = 50)
    private String targetType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_data", nullable = false)
    private String targetData;

    @Column(name = "requested_by", columnDefinition = "CHAR(36)", nullable = false)
    private String requestedBy;

    @CreationTimestamp
    @Column(name = "requested_at", updatable = false)
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "reviewed_by", columnDefinition = "CHAR(36)")
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        if (status == null) {
            status = RequestStatus.pending;
        }
    }
}