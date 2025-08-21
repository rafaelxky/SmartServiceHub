package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartServiceHub {
    public static void main(String[] args) {
        System.out.println("Started");
        SpringApplication.run(SmartServiceHub.class, args);
    }
}

