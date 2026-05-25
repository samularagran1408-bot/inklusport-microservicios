package com.inklusport.sports.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "event_registration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistration {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id = UUID.randomUUID().toString();

    @Column(name = "user_id", columnDefinition = "CHAR(36)", nullable = false)
    private String userId;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @CreationTimestamp
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "attended")
    private Boolean attended = false;

    @Column(name = "waitlist_position")
    private Integer waitlistPosition;

    @Column(name = "qr_code", columnDefinition = "TEXT")
    private String qrCode;

    @OneToOne(mappedBy = "registration", cascade = CascadeType.ALL)
    private EventAttendance attendance;
}