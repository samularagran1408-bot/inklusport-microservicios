package com.inklusport.ai.repository;

import com.inklusport.ai.model.BiomechanicalAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BiomechanicalAnalysisRepository extends MongoRepository<BiomechanicalAnalysis, String> {
    
    List<BiomechanicalAnalysis> findByUsuarioIdOrderByFechaDesc(String usuarioId);
    
    List<BiomechanicalAnalysis> findByUsuarioIdAndDisabilityType(String usuarioId, String disabilityType);
}