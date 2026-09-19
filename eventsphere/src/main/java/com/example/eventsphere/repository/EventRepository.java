package com.example.eventsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventsphere.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByOrderByEventDateAsc();
}