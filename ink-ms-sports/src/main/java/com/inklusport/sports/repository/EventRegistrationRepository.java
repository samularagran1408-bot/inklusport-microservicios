package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, String> {

    List<EventRegistration> findByUserId(String userId);

    List<EventRegistration> findByEventId(String eventId);

    Optional<EventRegistration> findByUserIdAndEventId(String userId, String eventId);

    boolean existsByUserIdAndEventId(String userId, String eventId);

    @Query("SELECT COUNT(e) FROM EventRegistration e WHERE e.event.id = :eventId")
    long countByEventId(@Param("eventId") String eventId);

    List<EventRegistration> findByEventIdAndAttendedTrue(String eventId);

    List<EventRegistration> findByEventIdAndAttendedFalse(String eventId);

    @Modifying
    @Transactional
    @Query("UPDATE EventRegistration e SET e.attended = true WHERE e.id = :registrationId")
    void markAsAttended(@Param("registrationId") String registrationId);

    @Modifying
    @Transactional
    @Query("DELETE FROM EventRegistration e WHERE e.event.id = :eventId")
    void deleteByEventId(@Param("eventId") String eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.availableCapacity = e.availableCapacity - 1 WHERE e.id = :eventId AND e.availableCapacity > 0")
    int decrementAvailableCapacity(@Param("eventId") String eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.availableCapacity = e.availableCapacity + 1 WHERE e.id = :eventId")
    int incrementAvailableCapacity(@Param("eventId") String eventId);
}