package com.example.eventsphere.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eventsphere.model.Event;
import com.example.eventsphere.model.Registration;
import com.example.eventsphere.model.RegistrationStatus;
import com.example.eventsphere.model.User;

public interface RegistrationRepository
        extends JpaRepository<Registration, Long> {

    List<Registration> findByUserOrderByRegistrationDateDesc(User user);

    List<Registration> findByEvent(Event event);

    Optional<Registration> findByUserAndEvent(
            User user,
            Event event
    );

    long countByEventAndStatus(
            Event event,
            RegistrationStatus status
    );

    void deleteByEvent(Event event);
}