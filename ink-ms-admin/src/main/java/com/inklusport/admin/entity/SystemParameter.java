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
@Table(name = "system_parameters")
public class SystemParameter {

    public enum ParamType {
        integer, boolean, string
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "param_key", unique = true, nullable = false, length = 100)
    private String paramKey;

    @Column(name = "param_value", nullable = false, length = 255)
    private String paramValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "param_type", columnDefinition = "ENUM('integer', 'boolean', 'string')")
    @Builder.Default
    private ParamType paramType = ParamType.string;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "updated_by", length = 36)
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}