package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventAttendanceRepository extends JpaRepository<EventAttendance, String> {

    Optional<EventAttendance> findByRegistration_Id(String registrationId);
}
