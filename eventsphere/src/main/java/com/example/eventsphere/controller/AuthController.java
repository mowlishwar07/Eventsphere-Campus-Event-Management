package com.example.eventsphere.controller;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.eventsphere.model.Role;
import com.example.eventsphere.model.User;
import com.example.eventsphere.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserService userService,
            PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================
    // HOME PAGE
    // =========================

    @GetMapping("/")
    public String home() {

        return "index";
    }


    // =========================
    // STUDENT REGISTRATION PAGE
    // =========================

    @GetMapping("/register")
    public String showRegister(
            Model model) {

        model.addAttribute(
                "user",
                new User()
        );

        return "register";
    }


    // =========================
    // STUDENT REGISTRATION
    // =========================

    @PostMapping("/register")
    public String register(
            @ModelAttribute User user,
            Model model) {

        String email =
                user.getEmail()
                        .trim()
                        .toLowerCase();

        Optional<User> existingUser =
                userService
                        .getUserByEmail(email);

        if (existingUser.isPresent()) {

            model.addAttribute(
                    "error",
                    "Email is already registered."
            );

            return "register";
        }

        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(
                        user.getPassword()
                )
        );

        // Every normal registration
        // becomes STUDENT
        user.setRole(Role.STUDENT);

        userService.saveUser(user);

        return "redirect:/login?registered";
    }


    // =========================
    // STUDENT LOGIN PAGE
    // =========================

    @GetMapping("/login")
    public String showStudentLogin() {

        return "login";
    }


    // =========================
    // STUDENT LOGIN
    // =========================

    @PostMapping("/login")
    public String studentLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        email = email
                .trim()
                .toLowerCase();

        Optional<User> optionalUser =
                userService
                        .getUserByEmail(email);

        if (optionalUser.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Invalid email or password."
            );

            return "login";
        }

        User user =
                optionalUser.get();

        // Block admin from student login
        if (user.getRole() != Role.STUDENT) {

            model.addAttribute(
                    "error",
                    "Admin account detected. Please use Admin Login."
            );

            return "login";
        }

        boolean passwordMatches =
                passwordEncoder.matches(
                        password,
                        user.getPassword()
                );

        if (!passwordMatches) {

            model.addAttribute(
                    "error",
                    "Invalid email or password."
            );

            return "login";
        }

        session.setAttribute(
                "userId",
                user.getId()
        );

        session.setAttribute(
                "userName",
                user.getName()
        );

        session.setAttribute(
                "role",
                user.getRole()
        );

        return "redirect:/student/dashboard";
    }


    // =========================
    // ADMIN LOGIN PAGE
    // =========================

    @GetMapping("/admin/login")
    public String showAdminLogin() {

        return "admin-login";
    }


    // =========================
    // ADMIN LOGIN
    // =========================

    @PostMapping("/admin/login")
    public String adminLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        email = email
                .trim()
                .toLowerCase();

        Optional<User> optionalUser =
                userService
                        .getUserByEmail(email);

        if (optionalUser.isEmpty()) {

            model.addAttribute(
                    "error",
                    "Invalid admin email or password."
            );

            return "admin-login";
        }

        User user =
                optionalUser.get();

        // Only admins can use this page
        if (user.getRole() != Role.ADMIN) {

            model.addAttribute(
                    "error",
                    "This account does not have admin access."
            );

            return "admin-login";
        }

        boolean passwordMatches =
                passwordEncoder.matches(
                        password,
                        user.getPassword()
                );

        if (!passwordMatches) {

            model.addAttribute(
                    "error",
                    "Invalid admin email or password."
            );

            return "admin-login";
        }

        session.setAttribute(
                "userId",
                user.getId()
        );

        session.setAttribute(
                "userName",
                user.getName()
        );

        session.setAttribute(
                "role",
                user.getRole()
        );

        return "redirect:/admin/dashboard";
    }


    // =========================
    // LOGOUT
    // =========================

    @GetMapping("/logout")
    public String logout(
            HttpSession session) {

        session.invalidate();

        return "redirect:/";
    }
}