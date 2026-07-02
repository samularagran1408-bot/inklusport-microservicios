package com.inklusport.admin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserRoleId implements Serializable {

    @Column(name = "admin_id", columnDefinition = "CHAR(36)")
    private String adminId;

    @Column(name = "role_id")
    private Integer roleId;
}