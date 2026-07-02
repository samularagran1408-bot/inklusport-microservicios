package com.inklusport.admin.repository;

import com.inklusport.admin.entity.AdminUserRole;
import com.inklusport.admin.entity.AdminUserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdminUserRoleRepository extends JpaRepository<AdminUserRole, AdminUserRoleId> {
    
    List<AdminUserRole> findByIdAdminId(String adminId);
    
    List<AdminUserRole> findByIdRoleId(Integer roleId);
    
    /**
     * Busca los roles asignados a un administrador
     * El query devuelve una lista de roles con el nombre de cada uno en la BD
     */
    @Query("SELECT r.name FROM AdminUserRole ur JOIN ur.role r WHERE ur.id.adminId = :adminId")
    List<String> findRoleNamesByAdminId(@Param("adminId") String adminId);
    
    /**
     * Busca los roles asignados a un administrador
     * Modifica los roles asignados a un administrador
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM AdminUserRole ur WHERE ur.id.adminId = :adminId")
    void deleteByAdminId(@Param("adminId") String adminId);
}