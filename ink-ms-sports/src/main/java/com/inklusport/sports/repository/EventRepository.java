package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByStatus(Event.EventStatus status);

    List<Event> findBySportId(Long sportId);

    List<Event> findByEventDate(LocalDate eventDate);

    List<Event> findByEventDateBetween(LocalDate startDate, LocalDate endDate);

    List<Event> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT e FROM Event e WHERE e.status = 'active' AND e.eventDate >= :today ORDER BY e.eventDate ASC, e.eventTime ASC")
    List<Event> findUpcomingEvents(@Param("today") LocalDate today);

    @Query("SELECT e FROM Event e WHERE e.sport.id = :sportId AND e.status = 'active' AND e.eventDate >= :today")
    List<Event> findUpcomingEventsBySport(@Param("sportId") Long sportId, @Param("today") LocalDate today);

    @Query("SELECT e FROM Event e WHERE e.status = 'active' AND e.eventDate BETWEEN :startDate AND :endDate ORDER BY e.eventDate ASC")
    List<Event> findEventsInDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.availableCapacity = e.availableCapacity - 1 WHERE e.id = :eventId AND e.availableCapacity > 0")
    int decrementAvailableCapacity(@Param("eventId") String eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.availableCapacity = e.availableCapacity + 1 WHERE e.id = :eventId")
    int incrementAvailableCapacity(@Param("eventId") String eventId);
}