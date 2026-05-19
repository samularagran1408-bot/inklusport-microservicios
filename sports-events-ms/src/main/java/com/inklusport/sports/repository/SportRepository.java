package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, String> {
}
