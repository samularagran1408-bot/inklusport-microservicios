package com.inklusport.sports.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sport")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private Difficulty difficulty = Difficulty.medio;

    @Column(name = "required_materials", columnDefinition = "TEXT")
    private String requiredMaterials;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sport", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SportDisability> disabilities = new ArrayList<>();

    @OneToMany(mappedBy = "sport")
    private List<Event> events = new ArrayList<>();

    public enum Difficulty {
        bajo, medio, alto
    }
}