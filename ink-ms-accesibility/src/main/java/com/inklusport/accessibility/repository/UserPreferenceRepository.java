package com.inklusport.accessibility.repository;

import com.inklusport.accessibility.model.UserPreference;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends MongoRepository<UserPreference, String> {
    Optional<UserPreference> findByUserId(String userId);
    void deleteByUserId(String userId);
    boolean existsByUserId(String userId);
}