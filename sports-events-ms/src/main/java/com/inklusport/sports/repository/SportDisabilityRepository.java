package com.inklusport.sports.repository;

import com.inklusport.sports.entity.SportDisability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SportDisabilityRepository extends JpaRepository<SportDisability, String> {

    List<SportDisability> findBySport_Id(String sportId);

    boolean existsBySport_IdAndDisability_Id(String sportId, String disabilityId);
}
