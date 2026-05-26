package com.inklusport.sports.entity;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

@Entity
@Table(name = "sport_disability")
@Data
@Builder
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

    @Column(name = "adaptations", columnDefinition = "TEXT", nullable = false)
    private String adaptations;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SportDisabilityId implements Serializable {
        @Column(name = "sport_id")
        private Integer sportId;
        
        @Column(name = "disability_id")
        private Integer disabilityId;
    }
}