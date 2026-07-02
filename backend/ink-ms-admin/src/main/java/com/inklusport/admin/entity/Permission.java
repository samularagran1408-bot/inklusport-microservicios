package com.inklusport.admin.entity;

import com.inklusport.admin.enums.PermissionAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "permission")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String resource;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PermissionAction action;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private List<RolePermission> roles = new ArrayList<>();
}