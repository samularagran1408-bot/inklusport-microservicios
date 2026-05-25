package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Disability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DisabilityRepository extends JpaRepository<Disability, Long> {

    Optional<Disability> findByName(String name);

    boolean existsByName(String name);

    List<Disability> findByIsActiveTrue();

    List<Disability> findByCategory(String category);

    @Query("SELECT d FROM Disability d WHERE d.isActive = true AND d.name LIKE %:keyword%")
    List<Disability> searchByName(@Param("keyword") String keyword);
}