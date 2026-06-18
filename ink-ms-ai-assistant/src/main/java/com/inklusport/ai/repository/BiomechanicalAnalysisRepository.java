package com.inklusport.ai.repository;

import com.inklusport.ai.model.BiomechanicalAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BiomechanicalAnalysisRepository extends MongoRepository<BiomechanicalAnalysis, String> {
    
    List<BiomechanicalAnalysis> findByUsuarioId(String usuarioId);

    Page<BiomechanicalAnalysis> findByUsuarioId(String usuarioId, Pageable pageable);
    
    List<BiomechanicalAnalysis> findByPlanId(String planId);
    
    List<BiomechanicalAnalysis> findByDisabilityType(String disabilityType);
    
    List<BiomechanicalAnalysis> findBySessionId(String sessionId);
    
    List<BiomechanicalAnalysis> findByFechaBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'puntaje_general': { $gte: 70 } }")
    List<BiomechanicalAnalysis> findHighScoreAnalyses();
    
    @Query("{ 'puntaje_general': { $lt: 50 } }")
    List<BiomechanicalAnalysis> findLowScoreAnalyses();
    
    List<BiomechanicalAnalysis> findByUsuarioIdAndFechaBetween(String usuarioId, LocalDateTime start, LocalDateTime end);
    
    @Query(value = "{ 'usuario_id': ?0 }", sort = "{ 'fecha': -1 }")
    List<BiomechanicalAnalysis> findLatestByUsuarioId(String usuarioId);
    
    List<BiomechanicalAnalysis> findByEjercicioNombre(String ejercicioNombre);
    
    @Query("{ 'recomendaciones': { $exists: true, $ne: '' } }")
    List<BiomechanicalAnalysis> findWithRecommendations();
    
    List<BiomechanicalAnalysis> findByPlanIdAndDisabilityType(String planId, String disabilityType);
    
    @Query(value = "{ 'usuario_id': ?0 }", fields = "{ 'puntaje_general': 1 }")
    List<BiomechanicalAnalysis> findScoresByUsuarioId(String usuarioId);
    
    @Query("{ 'rango_movimiento': { $lt: 40 } }")
    List<BiomechanicalAnalysis> findLowRangeMotion();
    
    @Query("{ 'simetria': { $lt: 50 } }")
    List<BiomechanicalAnalysis> findLowSymmetry();
    
    void deleteByFechaBefore(LocalDateTime date);
}