package com.example.eventsphere.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventsphere.model.Event;
import com.example.eventsphere.model.EventStatus;
import com.example.eventsphere.model.Registration;
import com.example.eventsphere.model.RegistrationStatus;
import com.example.eventsphere.model.Role;
import com.example.eventsphere.model.User;
import com.example.eventsphere.service.EventService;
import com.example.eventsphere.service.RegistrationService;
import com.example.eventsphere.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final UserService userService;
    private final EventService eventService;
    private final RegistrationService registrationService;

    public StudentController(
            UserService userService,
            EventService eventService,
            RegistrationService registrationService) {

        this.userService = userService;
        this.eventService = eventService;
        this.registrationService = registrationService;
    }

    private boolean isStudent(HttpSession session) {

        return session.getAttribute("userId") != null
                && Role.STUDENT.equals(
                        session.getAttribute("role")
                );
    }

    @GetMapping("/dashboard")
    public String dashboard(
            HttpSession session,
            Model model) {

        if (!isStudent(session)) {
            return "redirect:/login";
        }

        Long userId =
                (Long) session.getAttribute("userId");

        Optional<User> user =
                userService.getUserById(userId);

        if (user.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", user.get());

        return "student-dashboard";
    }

    @GetMapping("/events")
    public String events(
            @RequestParam(name = "keyword", required = false) String keyword,
            HttpSession session,
            Model model) {

        if (!isStudent(session)) {
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

        return "student-events";
    }

    @GetMapping("/events/{eventId}/register")
    public String showEventRegistration(
            @PathVariable Long eventId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!isStudent(session)) {
            return "redirect:/login";
        }

        Long userId =
                (Long) session.getAttribute("userId");

        Optional<User> userOptional =
                userService.getUserById(userId);

        Optional<Event> eventOptional =
                eventService.getEventById(eventId);

        if (userOptional.isEmpty()
                || eventOptional.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Event or user not found."
            );

            return "redirect:/student/events";
        }

        Event event = eventOptional.get();
        User user = userOptional.get();

        if (event.getStatus() != EventStatus.OPEN) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Registration is closed for this event."
            );

            return "redirect:/student/events";
        }

        Optional<Registration> existing =
                registrationService
                        .findByUserAndEvent(user, event);

        if (existing.isPresent()
                && existing.get().getStatus()
                        == RegistrationStatus.REGISTERED) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "You have already registered for this event."
            );

            return "redirect:/student/events";
        }

        model.addAttribute("event", event);
        model.addAttribute("user", user);

        return "event-register";
    }

    @PostMapping("/events/{eventId}/register")
    public String registerForEvent(
            @PathVariable Long eventId,
            @RequestParam(name = "teamName", required = false) String teamName,
            @RequestParam(name = "memberRollNo", required = false) List<String> memberRollNos,
            @RequestParam(name = "memberName", required = false) List<String> memberNames,
            @RequestParam(name = "memberDept", required = false) List<String> memberDepts,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isStudent(session)) {
            return "redirect:/login";
        }

        Long userId =
                (Long) session.getAttribute("userId");

        Optional<User> userOptional =
                userService.getUserById(userId);

        Optional<Event> eventOptional =
                eventService.getEventById(eventId);

        if (userOptional.isEmpty()
                || eventOptional.isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "User or event not found."
            );

            return "redirect:/student/events";
        }

        User user = userOptional.get();
        Event event = eventOptional.get();

        if (event.getStatus() != EventStatus.OPEN) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Registration is closed for this event."
            );

            return "redirect:/student/events";
        }

        long registeredCount =
                registrationService
                        .getRegisteredCount(event);

        Optional<Registration> existing =
                registrationService
                        .findByUserAndEvent(user, event);

        // Build structured team members roster if team event or team info submitted
        String formattedTeamMembers = null;
        if (event.isTeamEvent()
                || (teamName != null && !teamName.trim().isEmpty())) {

            StringBuilder sb = new StringBuilder();
            sb.append("Leader: ")
              .append(user.getRollNumber() != null ? user.getRollNumber() : "N/A")
              .append(" - ")
              .append(user.getName())
              .append(" (")
              .append(user.getDepartment())
              .append(")");

            if (memberRollNos != null && memberNames != null) {
                int count = 2;
                for (int i = 0; i < memberRollNos.size(); i++) {
                    String roll = memberRollNos.get(i) != null ? memberRollNos.get(i).trim() : "";
                    String name = i < memberNames.size() && memberNames.get(i) != null ? memberNames.get(i).trim() : "";
                    String dept = (memberDepts != null && i < memberDepts.size() && memberDepts.get(i) != null)
                            ? memberDepts.get(i).trim() : "";

                    if (!roll.isEmpty() || !name.isEmpty()) {
                        sb.append("\nMember ").append(count++).append(": ")
                          .append(roll.isEmpty() ? "N/A" : roll)
                          .append(" - ")
                          .append(name.isEmpty() ? "N/A" : name)
                          .append(" (")
                          .append(dept.isEmpty() ? "N/A" : dept)
                          .append(")");
                    }
                }
            }
            formattedTeamMembers = sb.toString();
        }

        Registration registration;

        if (existing.isPresent()) {

            registration = existing.get();

            if (registration.getStatus()
                    == RegistrationStatus.REGISTERED) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "You already registered for this event."
                );

                return "redirect:/student/events";
            }

            if (registeredCount
                    >= event.getMaxParticipants()) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Event is full."
                );

                return "redirect:/student/events";
            }

            registration.setStatus(
                    RegistrationStatus.REGISTERED
            );

            registration.setRegistrationDate(
                    LocalDate.now()
            );

        } else {

            if (registeredCount
                    >= event.getMaxParticipants()) {

                redirectAttributes.addFlashAttribute(
                        "error",
                        "Event is full."
                );

                return "redirect:/student/events";
            }

            registration = new Registration();
            registration.setUser(user);
            registration.setEvent(event);
            registration.setRegistrationDate(
                    LocalDate.now()
            );
            registration.setStatus(
                    RegistrationStatus.REGISTERED
            );
        }

        if (teamName != null && !teamName.trim().isEmpty()) {
            registration.setTeamName(teamName.trim());
        } else if (event.isTeamEvent()) {
            registration.setTeamName("Team " + user.getName());
        }

        registration.setTeamMembers(formattedTeamMembers);

        registrationService
                .saveRegistration(registration);

        redirectAttributes.addFlashAttribute(
                "success",
                event.isTeamEvent()
                        ? "Team registration submitted successfully!"
                        : "Event registered successfully!"
        );

        return "redirect:/student/registrations";
    }

    @GetMapping("/registrations")
    public String myRegistrations(
            HttpSession session,
            Model model) {

        if (!isStudent(session)) {
            return "redirect:/login";
        }

        Long userId =
                (Long) session.getAttribute("userId");

        Optional<User> user =
                userService.getUserById(userId);

        if (user.isEmpty()) {
            return "redirect:/login";
        }

        model.addAttribute(
                "registrations",
                registrationService
                        .getRegistrationsByUser(
                                user.get()
                        )
        );

        return "student-registrations";
    }

    @PostMapping("/registrations/{id}/cancel")
    public String cancelRegistration(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!isStudent(session)) {
            return "redirect:/login";
        }

        Long userId =
                (Long) session.getAttribute("userId");

        Optional<Registration> optional =
                registrationService
                        .getRegistrationById(id);

        if (optional.isEmpty()) {
            return "redirect:/student/registrations";
        }

        Registration registration =
                optional.get();

        if (!registration.getUser()
                .getId()
                .equals(userId)) {

            return "redirect:/student/registrations";
        }

        registration.setStatus(
                RegistrationStatus.CANCELLED
        );

        registrationService
                .saveRegistration(registration);

        redirectAttributes.addFlashAttribute(
                "success",
                "Registration cancelled."
        );

        return "redirect:/student/registrations";
    }
}