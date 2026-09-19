package com.example.eventsphere.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eventsphere.model.Event;
import com.example.eventsphere.model.Registration;
import com.example.eventsphere.model.RegistrationStatus;
import com.example.eventsphere.model.User;
import com.example.eventsphere.repository.RegistrationRepository;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    public RegistrationService(
            RegistrationRepository registrationRepository) {

        this.registrationRepository = registrationRepository;
    }

    public Registration saveRegistration(
            Registration registration) {

        return registrationRepository.save(registration);
    }

    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAll();
    }

    public Optional<Registration> getRegistrationById(Long id) {
        return registrationRepository.findById(id);
    }

    public List<Registration> getRegistrationsByUser(User user) {

        return registrationRepository
                .findByUserOrderByRegistrationDateDesc(user);
    }

    public List<Registration> getRegistrationsByEvent(Event event) {

        return registrationRepository.findByEvent(event);
    }

    public Optional<Registration> findByUserAndEvent(
            User user,
            Event event) {

        return registrationRepository
                .findByUserAndEvent(user, event);
    }

    public long getRegisteredCount(Event event) {

        return registrationRepository
                .countByEventAndStatus(
                        event,
                        RegistrationStatus.REGISTERED
                );
    }

    public void deleteRegistration(Long id) {
        registrationRepository.deleteById(id);
    }

    @Transactional
    public void deleteRegistrationsByEvent(Event event) {
        registrationRepository.deleteByEvent(event);
    }
}