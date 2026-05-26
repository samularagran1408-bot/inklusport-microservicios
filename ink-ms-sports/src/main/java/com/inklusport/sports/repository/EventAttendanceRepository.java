package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, String> {

    boolean existsByRegistrationId(String registrationId);

    List<EventAttendance> findByRegistrationId(String registrationId);

    List<EventAttendance> findByRegistration_EventId(String eventId);
}