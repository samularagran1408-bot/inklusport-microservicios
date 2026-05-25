package com.inklusport.sports.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event_attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventAttendance {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id = UUID.randomUUID().toString();

    @OneToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private EventRegistration registration;

    @CreationTimestamp
    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_in_method")
    private CheckInMethod checkInMethod = CheckInMethod.qr;

    @Column(name = "verified_by", columnDefinition = "CHAR(36)")
    private String verifiedBy;

    public enum CheckInMethod {
        qr, manual, admin
    }
}