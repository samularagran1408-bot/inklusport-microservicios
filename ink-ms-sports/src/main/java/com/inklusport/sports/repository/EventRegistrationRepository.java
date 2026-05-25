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

    @Query("SELECT COUNT(e) FROM EventRegistration e WHERE e.eventId = :eventId AND e.waitlistPosition IS NULL")
    long countConfirmedRegistrations(@Param("eventId") String eventId);

    Optional<EventRegistration> findByQrCode(String qrCode);

    @Modifying
    @Transactional
    @Query("UPDATE EventRegistration e SET e.attended = true WHERE e.id = :registrationId")
    void markAsAttended(@Param("registrationId") String registrationId);
}