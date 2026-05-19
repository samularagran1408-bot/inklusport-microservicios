package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WaitlistRepository extends JpaRepository<Waitlist, String> {

    List<Waitlist> findByEvent_IdOrderByPositionAsc(String eventId);

    Optional<Waitlist> findByEvent_IdAndUserId(String eventId, String userId);

    boolean existsByEvent_IdAndUserId(String eventId, String userId);

    long countByEvent_Id(String eventId);
}
