package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatFeedbackRepository extends MongoRepository<ChatFeedback, String> {
    
    List<ChatFeedback> findByUsuarioId(String usuarioId);
    
    List<ChatFeedback> findByConversacionId(String conversacionId);
}