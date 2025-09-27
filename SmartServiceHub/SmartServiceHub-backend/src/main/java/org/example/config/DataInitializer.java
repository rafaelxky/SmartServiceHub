package org.example.config;

import org.example.models.AppUser;
import org.example.models.Roles;
import org.example.services.persistance.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        EnvSetup.setupAdminUser();
        return args -> {
            if (userRepository.count() == 0) {
                AppUser admin = new AppUser(
                        null,
                        System.getenv("ADMIN_NAME"),
                        System.getenv("ADMIN_MAIL"),
                        passwordEncoder.encode(System.getenv("ADMIN_PASSWORD")),
                        Roles.ADMIN.getRoleName(),
                        null
                );

                userRepository.save(admin);
                System.out.println("Initial admin user created: " + System.getenv("ADMIN_NAME") + "/" + System.getenv("ADMIN_PASSWORD"));
            }
        };
    }
}
