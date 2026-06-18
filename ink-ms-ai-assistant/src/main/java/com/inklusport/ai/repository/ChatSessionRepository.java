package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatSession;
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
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {
    
    List<ChatSession> findByUsuarioId(String usuarioId);

    Page<ChatSession> findByUsuarioId(String usuarioId, Pageable pageable);
    
    @Query("{ 'usuario_id': ?0, 'estado': 'activa' }")
    Optional<ChatSession> findActiveSessionByUsuarioId(String usuarioId);
    
    Optional<ChatSession> findByIdAndUsuarioId(String id, String usuarioId);
    
    List<ChatSession> findByEstado(String estado);
    
    List<ChatSession> findByDisabilityType(String disabilityType);
    
    @Query("{ 'estado': 'abandonada', 'ultima_interaccion': { $lt: ?0 } }")
    List<ChatSession> findAbandonedSessions(LocalDateTime date);
    
    @Query("{ 'ultima_interaccion': { $lt: ?0 }, 'estado': 'activa' }")
    List<ChatSession> findInactiveSessions(LocalDateTime date);
    
    List<ChatSession> findByFechaInicioBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'mensajes': { $exists: true, $ne: [] } }")
    List<ChatSession> findSessionsWithMessages();

    List<ChatSession> findByUsuarioIdAndEstado(String usuarioId, String estado);
    
    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'estado': ?1, 'fecha_fin': ?2 } }")
    void updateSessionStatus(String sessionId, String estado, LocalDateTime fechaFin);
    
    @Query("{ '_id': ?0 }")
    @Update("{ '$set': { 'ultima_interaccion': ?1 } }")
    void updateLastInteraction(String sessionId, LocalDateTime lastInteraction);
    
    long countByUsuarioIdAndEstado(String usuarioId, String estado);
    
    List<ChatSession> findByDisabilityTypeAndEstado(String disabilityType, String estado);
}