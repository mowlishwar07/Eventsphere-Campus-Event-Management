package com.example.eventsphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.eventsphere.model.Event;
import com.example.eventsphere.repository.EventRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAllByOrderByEventDateAsc();
    }

    public List<Event> searchEvents(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return eventRepository.searchEvents(keyword.trim());
        }
        return getAllEvents();
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}