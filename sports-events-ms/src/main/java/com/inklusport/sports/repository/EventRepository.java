package com.inklusport.sports.repository;

import com.inklusport.sports.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, String> {

    List<Event> findByStartDateBetween(LocalDateTime from, LocalDateTime to);

    List<Event> findBySport_Id(String sportId);
}
