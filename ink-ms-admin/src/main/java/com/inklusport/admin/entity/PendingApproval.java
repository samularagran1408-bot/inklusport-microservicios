package main.java.com.inklusport.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pending_approval")
public class PendingApproval {

    public enum ApprovalStatus {
        pending, approved, rejected
    }

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "target_type", nullable = false, length = 50)
    private String targetType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_data", nullable = false, columnDefinition = "json")
    private Map<String, Object> targetData;

    @Column(name = "requested_by", nullable = false, length = 36)
    private String requestedBy;

    @Column(name = "requested_at", updatable = false)
    private LocalDateTime requestedAt;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('pending', 'approved', 'rejected')")
    @Builder.Default
    private ApprovalStatus status = ApprovalStatus.pending;

    @Column(name = "reviewed_by", length = 36)
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = java.util.UUID.randomUUID().toString();
        }
        this.requestedAt = LocalDateTime.now();
    }
}