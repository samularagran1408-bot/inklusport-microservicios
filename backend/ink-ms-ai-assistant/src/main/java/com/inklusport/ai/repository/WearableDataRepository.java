package com.inklusport.ai.repository;

import com.inklusport.ai.model.WearableData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WearableDataRepository extends MongoRepository<WearableData, String> {
}
