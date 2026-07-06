package com.inklusport.ai.repository;

import com.inklusport.ai.model.BiomechanicalAnalysis;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BiomechanicalAnalysisRepository extends MongoRepository<BiomechanicalAnalysis, String> {
    List<BiomechanicalAnalysis> findByUsuarioIdOrderByCreatedAtDesc(String usuarioId);
}
