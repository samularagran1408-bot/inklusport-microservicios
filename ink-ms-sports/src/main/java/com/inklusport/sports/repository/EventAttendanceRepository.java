package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventAttendanceRepository extends JpaRepository<EventAttendance, String> {

    boolean existsByRegistrationId(String registrationId);

    List<EventAttendance> findByRegistrationId(String registrationId);

    List<EventAttendance> findByRegistration_EventId(String eventId);
}