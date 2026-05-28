package com.inklusport.admin.repository;

import com.inklusport.admin.entity.AiActionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AiActionLogRepository extends JpaRepository<AiActionLog, String> {
    
    /**
     * Page para paginación y ordenamiento de resultados en las consultas a la base de datos
     */
    Page<AiActionLog> findByUserId(String userId, Pageable pageable);
    
    Page<AiActionLog> findByAiFeature(String aiFeature, Pageable pageable);
    
    Page<AiActionLog> findByWasAppliedTrue(Pageable pageable);
    
    @Query("SELECT a FROM AiActionLog a WHERE a.confidence >= :minConfidence")
    List<AiActionLog> findByHighConfidence(@Param("minConfidence") BigDecimal minConfidence);
    
    /**
     * Busca las acciones más utilizadas
     * listadas en un mapa con la clave como la acción y el valor como el número de veces que se ejecutó
     */
     */
    @Query("SELECT a.aiFeature, COUNT(a), AVG(a.confidence) FROM AiActionLog a WHERE a.createdAt >= :since GROUP BY a.aiFeature")
    List<Object[]> getFeatureStats(@Param("since") LocalDateTime since);
}