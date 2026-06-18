package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatTraining;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatTrainingRepository extends MongoRepository<ChatTraining, String> {
    
    List<ChatTraining> findByIntencion(String intencion);
    
    List<ChatTraining> findByActivoTrue();
    
    List<ChatTraining> findByActivoTrueOrderByPrioridadDesc();
    
    @Query("{ 'palabras_clave': { $in: ?0 }, 'activo': true }")
    List<ChatTraining> findByPalabrasClaveIn(List<String> keywords);
    
    List<ChatTraining> findByIntencionAndActivoTrue(String intencion);
    
    Optional<ChatTraining> findByPreguntaIgnoreCase(String pregunta);
    
    @Query("{ 'palabras_clave': { $regex: ?0, $options: 'i' }, 'activo': true }")
    List<ChatTraining> findByPalabrasClaveRegex(String keyword);
    
    @Query("{ 'intencion': ?0, 'activo': true, 'prioridad': { $gte: 5 } }")
    List<ChatTraining> findHighPriorityByIntencion(String intencion);
    
    long countByActivoTrue();
    
    @Query("{ 'fecha_creacion': { $gte: ?0 }, 'activo': true }")
    List<ChatTraining> findByFechaCreacionAfter(java.time.LocalDateTime date);
    
    @Query("{ 'respuesta_adaptada.?0': { $exists: true }, 'activo': true }")
    List<ChatTraining> findByRespuestaAdaptadaForDisability(String disabilityType);
}