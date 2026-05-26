package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Disability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisabilityRepository extends JpaRepository<Disability, Integer> {
    List<Disability> findByIsActiveTrue();
    boolean existsByName(String name);
}