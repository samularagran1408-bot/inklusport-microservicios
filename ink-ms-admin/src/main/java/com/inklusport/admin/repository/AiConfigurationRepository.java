package com.inklusport.admin.repository;

import com.inklusport.admin.entity.AiConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiConfigurationRepository extends JpaRepository<AiConfiguration, Integer> {
    
    Optional<AiConfiguration> findByFeatureName(String featureName);

    boolean existsByFeatureName(String featureName);
    
    /**
     * Busca todas las configuraciones activadas
     */
    List<AiConfiguration> findByIsEnabledTrue();
    
    /**
     * Modifica el estado de activación de una característica de IA
     */
    @Modifying
    @Transactional
    @Query("UPDATE AiConfiguration a SET a.isEnabled = :enabled WHERE a.featureName = :featureName")
    void setEnabled(@Param("featureName") String featureName, @Param("enabled") Boolean enabled);
}