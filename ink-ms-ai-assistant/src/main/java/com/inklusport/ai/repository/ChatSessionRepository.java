package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {

    Optional<ChatSession> findByUsuarioIdAndEstado(String usuarioId, String estado);
}