package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Waitlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitlistRepository extends JpaRepository<Waitlist, String> {

    List<Waitlist> findByUserId(String userId);

    List<Waitlist> findByEventId(String eventId);

    Optional<Waitlist> findByUserIdAndEventId(String userId, String eventId);

    List<Waitlist> findByStatus(Waitlist.WaitlistStatus status);

    @Query("SELECT w FROM Waitlist w WHERE w.event.id = :eventId ORDER BY w.position ASC")
    List<Waitlist> findByEventIdOrderByPositionAsc(@Param("eventId") String eventId);

    @Query("SELECT MAX(w.position) FROM Waitlist w WHERE w.event.id = :eventId")
    Integer findMaxPositionByEventId(@Param("eventId") String eventId);

    @Query("SELECT COUNT(w) FROM Waitlist w WHERE w.event.id = :eventId AND w.notified = false")
    long countNotNotifiedByEventId(@Param("eventId") String eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Waitlist w SET w.notified = true, w.notifiedAt = CURRENT_TIMESTAMP WHERE w.id = :waitlistId")
    void markAsNotified(@Param("waitlistId") String waitlistId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Waitlist w WHERE w.event.id = :eventId")
    void deleteByEventId(@Param("eventId") String eventId);
}