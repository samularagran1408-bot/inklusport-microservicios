package com.inklusport.ai.repository;

import com.inklusport.ai.model.TrainingPlan;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TrainingPlanRepository extends MongoRepository<TrainingPlan, String> {
    List<TrainingPlan> findByUsuarioIdOrderByUpdatedAtDesc(String usuarioId);
    Optional<TrainingPlan> findByUsuarioIdAndEntrenadorId(String usuarioId, String entrenadorId);
}
