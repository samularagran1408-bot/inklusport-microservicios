package com.inklusport.ai.repository;

import com.inklusport.ai.model.DisabilityProfile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisabilityProfileRepository extends MongoRepository<DisabilityProfile, String> {
    Optional<DisabilityProfile> findByUserId(String userId);
}
