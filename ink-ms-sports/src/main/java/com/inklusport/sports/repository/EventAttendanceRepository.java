package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, String> {
    Optional<EventAttendance> findByRegistrationId(String registrationId);
}