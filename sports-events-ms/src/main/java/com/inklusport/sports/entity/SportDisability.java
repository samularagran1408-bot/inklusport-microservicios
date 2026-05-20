package com.inklusport.sports.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "sport_disability")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportDisability {

    @EmbeddedId
    private SportDisabilityId id;

    @ManyToOne
    @MapsId("sportId")
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne
    @MapsId("disabilityId")
    @JoinColumn(name = "disability_id")
    private Disability disability;

    @Column(name = "adaptations", nullable = false, columnDefinition = "TEXT")
    private String adaptations;
}