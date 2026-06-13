package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Event;
import com.inklusport.sports.enums.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {
    List<Event> findByStatus(EventStatus status);

    List<Event> findByEventDateAndStatus(LocalDate eventDate, EventStatus status);

    long countByStatus(EventStatus status);

    long countBySportIdAndStatus(Long sportId, EventStatus status);
}