package com.inklusport.admin.repository;

import com.inklusport.admin.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRole, Integer> {
    
    Optional<AdminRole> findByName(String name);
    
    boolean existsByName(String name);
}