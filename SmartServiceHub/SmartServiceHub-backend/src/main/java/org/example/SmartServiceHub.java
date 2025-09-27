package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.config.EnvSetup;
import org.example.lua.LuaModManager;
import org.luaj.vm2.LuaTable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartServiceHub {
    public static void main(String[] args) {
        System.out.println("Started");
        EnvSetup.setupDbEnv();
        SpringApplication.run(SmartServiceHub.class, args);
    }
}

