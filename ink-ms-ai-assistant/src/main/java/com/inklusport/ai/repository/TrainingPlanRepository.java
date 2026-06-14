package com.inklusport.ai.repository;

import com.inklusport.ai.model.TrainingPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingPlanRepository extends MongoRepository<TrainingPlan, String> {
    
    List<TrainingPlan> findByUsuarioIdAndActivoTrue(String usuarioId);
    
    List<TrainingPlan> findByEntrenadorId(String entrenadorId);
}