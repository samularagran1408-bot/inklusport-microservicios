package com.inklusport.sports.repository;

import com.inklusport.sports.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRegistrationRepository extends JpaRepository<EventRegistration, String> {

    long countByEvent_IdAndStatus(String eventId, String status);

    List<EventRegistration> findByEvent_Id(String eventId);

    List<EventRegistration> findByUserId(String userId);

    Optional<EventRegistration> findByEvent_IdAndUserId(String eventId, String userId);

    boolean existsByEvent_IdAndUserId(String eventId, String userId);
}
