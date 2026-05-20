package com.inklusport.sports.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "disability")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Disability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "disability", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SportDisability> sports = new ArrayList<>();
}