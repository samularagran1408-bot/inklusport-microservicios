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
    
    /**
     * Busca logs por ID de administrador
     */
    Page<AdminAuditLog> findByAdminId(String adminId, Pageable pageable);
    
    /**
     * Busca logs por acción
     */
    Page<AdminAuditLog> findByAction(String action, Pageable pageable);
    
    /**
     * Busca logs por rango de fechas
     */
    @Query("SELECT a FROM AdminAuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate")
    Page<AdminAuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate, 
                                         Pageable pageable);
    
    /**
     * Busca las acciones más utilizadas
     * listadas en un mapa con la clave como la acción y el valor como el número de veces que se ejecutó
     */
    @Query("SELECT a.action, COUNT(a) FROM AdminAuditLog a GROUP BY a.action")
    List<Object[]> countByAction();
    
    /**
     * Busca las acciones más utilizadas por día
     * listadas en un mapa con la clave como el día y el valor como el número de veces que se ejecutó
     */
    @Query("SELECT DATE(a.createdAt), COUNT(a) FROM AdminAuditLog a WHERE a.createdAt >= :since GROUP BY DATE(a.createdAt)")
    List<Object[]> countByDay(@Param("since") LocalDateTime since);
}