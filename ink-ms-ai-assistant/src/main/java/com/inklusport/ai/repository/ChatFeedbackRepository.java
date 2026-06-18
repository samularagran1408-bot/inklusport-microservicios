package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatFeedbackRepository extends MongoRepository<ChatFeedback, String> {
    
    List<ChatFeedback> findByConversacionId(String conversacionId);
    
    List<ChatFeedback> findByUsuarioId(String usuarioId);
    
    Optional<ChatFeedback> findByMensajeId(String mensajeId);
    
    @Query("{ 'util': true }")
    List<ChatFeedback> findUsefulFeedback();
    
    @Query("{ 'util': false }")
    List<ChatFeedback> findNotUsefulFeedback();
    
    List<ChatFeedback> findByUsuarioIdAndUtil(String usuarioId, Boolean util);
    
    List<ChatFeedback> findByFechaBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("{ 'comentario': { $exists: true, $ne: '' } }")
    List<ChatFeedback> findFeedbackWithComments();
    
    long countByUsuarioIdAndUtil(String usuarioId, Boolean util);
    
    @Query("{ 'fecha': { $gte: ?0 } }")
    List<ChatFeedback> findRecentFeedback(LocalDateTime date);
    
    List<ChatFeedback> findByConversacionIdAndUtil(String conversacionId, Boolean util);
    
    void deleteByConversacionId(String conversacionId);
}