package com.inklusport.ai.repository;

import com.inklusport.ai.model.ChatTraining;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatTrainingRepository extends MongoRepository<ChatTraining, String> {
    
    @Query("{ 'palabrasClave': { $in: ?0 }, 'activo': true }")
    List<ChatTraining> findByPalabrasClaveIn(List<String> palabrasClave);
    
    List<ChatTraining> findByIntencionAndActivoTrue(String intencion);
}