package com.inklusport.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin_user_role")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRole {

    @EmbeddedId
    private AdminUserRoleId id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private AdminRole role;

    @CreationTimestamp
    @Column(name = "assigned_at", updatable = false)
    private LocalDateTime assignedAt;

    @Column(name = "assigned_by", columnDefinition = "CHAR(36)")
    private String assignedBy;
}