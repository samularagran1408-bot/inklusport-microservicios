package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {

    Optional<Sport> findByName(String name);

    boolean existsByName(String name);

    List<Sport> findByIsActiveTrue();

    List<Sport> findByDifficulty(Sport.Difficulty difficulty);

    @Query("SELECT s FROM Sport s WHERE s.isActive = true AND s.name LIKE %:keyword%")
    List<Sport> searchByName(@Param("keyword") String keyword);

    @Query("SELECT s FROM Sport s JOIN s.disabilities d WHERE d.disability.id = :disabilityId AND s.isActive = true")
    List<Sport> findSportsByDisabilityId(@Param("disabilityId") Long disabilityId);
}