package com.inklusport.admin.repository;

import com.inklusport.admin.entity.Permission;
import com.inklusport.admin.enums.PermissionAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    
    Optional<Permission> findByName(String name);
    
    List<Permission> findByResource(String resource);
    
    List<Permission> findByAction(PermissionAction action);
    
    @Query("SELECT p FROM Permission p WHERE p.resource = :resource AND p.action = :action")
    Optional<Permission> findByResourceAndAction(@Param("resource") String resource, 
                                                  @Param("action") PermissionAction action);
    
    @Query("SELECT DISTINCT p.resource FROM Permission p")
    List<String> findAllResources();
}