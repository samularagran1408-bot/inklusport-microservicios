package com.inklusport.admin.repository;

import com.inklusport.admin.entity.SystemParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface SystemParameterRepository extends JpaRepository<SystemParameter, Integer> {
    
    Optional<SystemParameter> findByParamKey(String paramKey);
    
    boolean existsByParamKey(String paramKey);
    
    @Modifying
    @Transactional
    @Query("UPDATE SystemParameter s SET s.paramValue = :value, s.updatedBy = :updatedBy, s.updatedAt = CURRENT_TIMESTAMP WHERE s.paramKey = :key")
    void updateValue(@Param("key") String key, 
                     @Param("value") String value, 
                     @Param("updatedBy") String updatedBy);
}