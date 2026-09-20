package com.example.eventsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.eventsphere.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByEventDateAsc();

    @Query("SELECT e FROM Event e WHERE " +
           "LOWER(e.eventName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.category) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(e.venue) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY e.eventDate ASC")
    List<Event> searchEvents(@Param("keyword") String keyword);
}