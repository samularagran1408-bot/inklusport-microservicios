package com.inklusport.ai.repository;

import com.inklusport.ai.model.VoiceCommand;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceCommandRepository extends MongoRepository<VoiceCommand, String> {
}
