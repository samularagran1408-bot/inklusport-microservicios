package com.inklusport.admin.repository;

import com.inklusport.admin.entity.RolePermission;
import com.inklusport.admin.entity.RolePermission.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    
    List<RolePermission> findByIdRoleId(Integer roleId);
    
    @Query("SELECT rp.permission.name FROM RolePermission rp WHERE rp.id.roleId = :roleId")
    List<String> findPermissionNamesByRoleId(@Param("roleId") Integer roleId);
    
    /**
     * Modifying inica a Spring data JPA que una consulta ejecutada con 
     * @Query no es una simple lectura (SELECT), sino una operación 
     * de modificación en la base de datos, usadas es update, insert and select
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM RolePermission rp WHERE rp.id.roleId = :roleId")
    void deleteByRoleId(@Param("roleId") Integer roleId);
}