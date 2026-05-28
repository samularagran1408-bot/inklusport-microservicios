package com.inklusport.admin.repository;

import com.inklusport.admin.entity.AdminAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, String> {
    
    Page<AdminAuditLog> findByAdminId(String adminId, Pageable pageable);
    
    Page<AdminAuditLog> findByAction(String action, Pageable pageable);
    
    @Query("SELECT a FROM AdminAuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate")
    Page<AdminAuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate, 
                                         Pageable pageable);
    
    @Query("SELECT a.action, COUNT(a) FROM AdminAuditLog a GROUP BY a.action")
    List<Object[]> countByAction();
    
    @Query("SELECT DATE(a.createdAt), COUNT(a) FROM AdminAuditLog a WHERE a.createdAt >= :since GROUP BY DATE(a.createdAt)")
    List<Object[]> countByDay(@Param("since") LocalDateTime since);
}