package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SportRepository extends JpaRepository<Sport, Integer> {
    List<Sport> findByIsActiveTrue();
    boolean existsByName(String name);
}