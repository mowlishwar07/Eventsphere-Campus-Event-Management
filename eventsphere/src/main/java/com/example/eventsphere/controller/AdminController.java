package com.example.eventsphere.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventsphere.model.Event;
import com.example.eventsphere.model.EventStatus;
import com.example.eventsphere.model.Registration;
import com.example.eventsphere.model.Role;
import com.example.eventsphere.service.EventService;
import com.example.eventsphere.service.RegistrationService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventService eventService;
    private final RegistrationService registrationService;

    public AdminController(
            EventService eventService,
            RegistrationService registrationService) {

        this.eventService = eventService;
        this.registrationService = registrationService;
    }

    private boolean isAdmin(HttpSession session) {

        return session.getAttribute("userId") != null
                && Role.ADMIN.equals(
                        session.getAttribute("role")
                );
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        return "admin-dashboard";
    }

    @GetMapping("/events")
    public String events(
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Event> events;
        if (keyword != null && !keyword.trim().isEmpty()) {
            events = eventService.searchEvents(keyword.trim());
        } else {
            events = eventService.getAllEvents();
        }

        model.addAttribute("events", events);
        model.addAttribute("keyword", keyword);

        return "admin-events";
    }

    @GetMapping("/events/new")
    public String createEvent(
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Event event = new Event();
        event.setStatus(EventStatus.OPEN);

        model.addAttribute("event", event);

        return "event-form";
    }

    @PostMapping("/events/save")
    public String saveEvent(
            @ModelAttribute Event event,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        if (event.getStatus() == null) {
            event.setStatus(EventStatus.OPEN);
        }

        eventService.saveEvent(event);

        redirectAttributes.addFlashAttribute(
                "success",
                "Event saved successfully."
        );

        return "redirect:/admin/events";
    }

    @GetMapping("/events/edit/{id}")
    public String editEvent(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Optional<Event> event =
                eventService.getEventById(id);

        if (event.isEmpty()) {
            return "redirect:/admin/events";
        }

        model.addAttribute(
                "event",
                event.get()
        );

        return "event-form";
    }

    @GetMapping("/events/delete/{id}")
    public String deleteEvent(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Optional<Event> eventOptional =
                eventService.getEventById(id);

        if (eventOptional.isPresent()) {

            Event event = eventOptional.get();

            registrationService
                    .deleteRegistrationsByEvent(event);

            eventService.deleteEvent(id);
        }

        redirectAttributes.addFlashAttribute(
                "success",
                "Event deleted."
        );

        return "redirect:/admin/events";
    }

    @GetMapping("/registrations")
    public String registrations(
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<Registration> registrations;
        if (keyword != null && !keyword.trim().isEmpty()) {
            registrations = registrationService.searchRegistrations(keyword.trim());
        } else {
            registrations = registrationService.getAllRegistrations();
        }

        model.addAttribute("registrations", registrations);
        model.addAttribute("keyword", keyword);

        return "admin-registrations";
    }

    @GetMapping("/events/{id}/registrations")
    public String eventRegistrations(
            @PathVariable Long id,
            HttpSession session,
            Model model) {

        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        Optional<Event> eventOptional =
                eventService.getEventById(id);

        if (eventOptional.isEmpty()) {
            return "redirect:/admin/events";
        }

        Event event = eventOptional.get();

        model.addAttribute("event", event);

        model.addAttribute(
                "registrations",
                registrationService
                        .getRegistrationsByEvent(event)
        );

        return "event-registrations";
    }
}