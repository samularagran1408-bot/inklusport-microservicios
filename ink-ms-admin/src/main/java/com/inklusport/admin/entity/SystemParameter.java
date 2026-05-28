package com.inklusport.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_parameters")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "param_key", nullable = false, unique = true, length = 100)
    private String paramKey;

    @Column(name = "param_value", nullable = false, length = 255)
    private String paramValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "param_type")
    private ParamType paramType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "updated_by", columnDefinition = "CHAR(36)")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ParamType {
        integer, boolean, string
    }

    @PrePersist
    protected void onCreate() {
        if (paramType == null) {
            paramType = ParamType.string;
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}