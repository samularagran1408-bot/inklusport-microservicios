package com.inklusport.sports.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "event_time", nullable = false)
    private LocalTime eventTime;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "available_capacity", nullable = false)
    private Integer availableCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status = EventStatus.draft;

    @Column(name = "created_by", columnDefinition = "CHAR(36)")
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventRegistration> registrations = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Waitlist> waitlist = new ArrayList<>();

    public enum EventStatus {
        draft, active, cancelled, finished
    }
}