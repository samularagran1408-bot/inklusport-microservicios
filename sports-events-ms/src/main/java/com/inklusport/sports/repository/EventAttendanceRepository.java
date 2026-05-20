package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventAttendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventAttendanceRepository extends JpaRepository<EventAttendance, String> {

    Optional<EventAttendance> findByRegistrationId(String registrationId);

    List<EventAttendance> findByVerifiedBy(String verifiedBy);

    @Query("SELECT COUNT(a) FROM EventAttendance a WHERE a.checkInTime BETWEEN :startDate AND :endDate")
    long countByCheckInDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT a FROM EventAttendance a WHERE a.registration.event.id = :eventId")
    List<EventAttendance> findByEventId(@Param("eventId") String eventId);
}