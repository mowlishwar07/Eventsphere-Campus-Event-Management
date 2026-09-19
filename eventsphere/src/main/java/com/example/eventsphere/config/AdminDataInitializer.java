package com.example.eventsphere.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.eventsphere.model.Role;
import com.example.eventsphere.model.User;
import com.example.eventsphere.service.UserService;

@Configuration
public class AdminDataInitializer {

    @Bean
    public CommandLineRunner createAdmin(
            UserService userService,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail =
                    "admin@eventsphere.com";

            if (userService
                    .getUserByEmail(adminEmail)
                    .isEmpty()) {

                User admin =
                        new User();

                admin.setName(
                        "EventSphere Admin"
                );

                admin.setEmail(
                        adminEmail
                );

                admin.setPassword(
                        passwordEncoder.encode(
                                "admin123"
                        )
                );

                admin.setDepartment(
                        "Administration"
                );

                admin.setRole(
                        Role.ADMIN
                );

                userService.saveUser(admin);

                System.out.println(
                        "EventSphere Admin account created"
                );
            }
        };
    }
}