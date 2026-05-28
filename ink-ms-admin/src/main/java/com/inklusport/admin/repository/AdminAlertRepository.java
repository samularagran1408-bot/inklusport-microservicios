package com.inklusport.admin.repository;

import com.inklusport.admin.entity.AdminAlert;
import com.inklusport.admin.enums.AlertSeverity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AdminAlertRepository extends JpaRepository<AdminAlert, String> {
    
    /**
     * Busca alertas sin resolver
     */
    Page<AdminAlert> findByResolvedFalse(Pageable pageable);
    
    /**
     * Busca alertas por nivel de gravedad
     * Page able sirve para paginar y ordenar resultados en las consultas a la base de datos
     */
    Page<AdminAlert> findBySeverity(AlertSeverity severity, Pageable pageable);
    
    /**
     * Busca alertas por tipo
     */
    Page<AdminAlert> findByType(String type, Pageable pageable);
    
    /**
     * Busca alertas sin resolver por niveles de gravedad
     */
    @Query("SELECT a FROM AdminAlert a WHERE a.resolved = false AND a.severity IN :severities")
    List<AdminAlert> findUnresolvedBySeverities(@Param("severities") List<AlertSeverity> severities);
    
    /**
     * Marca alerta como resuelta
     * Modifying indica que esta consulta modifica datos en la base de datos
     */
    @Modifying
    @Transactional
    @Query("UPDATE AdminAlert a SET a.resolved = true, a.resolvedBy = :resolvedBy, a.resolvedAt = CURRENT_TIMESTAMP WHERE a.id = :id")
    void resolveAlert(@Param("id") String id, @Param("resolvedBy") String resolvedBy);
}