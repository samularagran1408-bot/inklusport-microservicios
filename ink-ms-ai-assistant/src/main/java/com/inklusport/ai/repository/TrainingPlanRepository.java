package com.inklusport.ai.repository;

import com.inklusport.ai.model.TrainingPlan;
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
public interface TrainingPlanRepository extends MongoRepository<TrainingPlan, String> {
    
    List<TrainingPlan> findByUsuarioId(String usuarioId);

    Page<TrainingPlan> findByUsuarioId(String usuarioId, Pageable pageable);
    
    List<TrainingPlan> findByUsuarioIdAndActivoTrue(String usuarioId);
    
    Optional<TrainingPlan> findFirstByUsuarioIdAndActivoTrue(String usuarioId);
    
    List<TrainingPlan> findByEntrenadorId(String entrenadorId);
    
    List<TrainingPlan> findByDisabilityType(String disabilityType);
    
    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    List<TrainingPlan> findByNombreRegex(String nombre);
    
    List<TrainingPlan> findByFechaInicioBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'progreso_porcentaje': { $gte: 80 } }")
    List<TrainingPlan> findHighProgressPlans();
    
    @Query("{ 'progreso_porcentaje': { $lt: 30 } }")
    List<TrainingPlan> findLowProgressPlans();
    
    @Query("{ 'fecha_fin': { $lte: ?0 }, 'activo': true }")
    List<TrainingPlan> findExpiringPlans(LocalDateTime date);
    
    @Query("{ 'fecha_fin': { $lt: ?0 }, 'activo': true }")
    List<TrainingPlan> findExpiredPlans(LocalDateTime date);
    
    List<TrainingPlan> findByObjetivo(String objetivo);
    
    @Query("{ 'sesiones_registradas': { $exists: true, $ne: [] } }")
    List<TrainingPlan> findWithSessions();
    
    List<TrainingPlan> findByUsuarioIdAndDisabilityType(String usuarioId, String disabilityType);
    
    @Query("{ 'progreso_porcentaje': { $gte: ?0, $lte: ?1 } }")
    List<TrainingPlan> findByProgresoBetween(Double min, Double max);
    
    long countByUsuarioIdAndActivoTrue(String usuarioId);
    
    @Query("{ 'ejercicios.nombre': { $regex: ?0, $options: 'i' } }")
    List<TrainingPlan> findByEjercicioNombre(String ejercicioNombre);
    
    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'progreso_porcentaje': ?1 } }")
    void updateProgreso(String planId, Double progreso);
    
    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'activo': false, 'updated_at': ?1 } }")
    void deactivatePlan(String planId, LocalDateTime updatedAt);
}