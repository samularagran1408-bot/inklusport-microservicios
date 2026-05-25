package com.inklusport.sports.repository;

import com.inklusport.sports.entity.SportDisability;
import com.inklusport.sports.entity.SportDisabilityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SportDisabilityRepository extends JpaRepository<SportDisability, SportDisabilityId> {

    List<SportDisability> findBySportId(Long sportId);

    List<SportDisability> findByDisabilityId(Long disabilityId);

    boolean existsBySportIdAndDisabilityId(Long sportId, Long disabilityId);

    @Modifying
    @Transactional
    void deleteBySportId(Long sportId);

    @Modifying
    @Transactional
    void deleteByDisabilityId(Long disabilityId);

    @Query("SELECT sd.disability.id FROM SportDisability sd WHERE sd.sport.id = :sportId")
    List<Long> findDisabilityIdsBySportId(@Param("sportId") Long sportId);

    @Query("SELECT sd.sport.id FROM SportDisability sd WHERE sd.disability.id = :disabilityId")
    List<Long> findSportIdsByDisabilityId(@Param("disabilityId") Long disabilityId);
}