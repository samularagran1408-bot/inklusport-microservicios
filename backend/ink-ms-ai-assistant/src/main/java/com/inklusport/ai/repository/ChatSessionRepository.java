package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatSessionRepository extends MongoRepository<ChatSession, String> {
}