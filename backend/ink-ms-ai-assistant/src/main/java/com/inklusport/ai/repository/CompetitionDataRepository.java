package com.inklusport.ai.repository;

import com.inklusport.ai.model.CompetitionData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompetitionDataRepository extends MongoRepository<CompetitionData, String> {
}
