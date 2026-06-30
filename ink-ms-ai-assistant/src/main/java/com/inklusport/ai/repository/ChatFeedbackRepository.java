package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatFeedback;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatFeedbackRepository extends MongoRepository<ChatFeedback, String> {

    List<ChatFeedback> findByMensajeId(String mensajeId);
}