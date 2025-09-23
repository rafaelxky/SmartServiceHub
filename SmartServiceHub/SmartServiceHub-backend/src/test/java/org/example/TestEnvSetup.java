package org.example;

import org.example.config.EnvSetup;
import org.hibernate.id.Configurable;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@TestConfiguration
public class TestEnvSetup implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(@NotNull ConfigurableApplicationContext context) {
        EnvSetup.setupDbEnv();
    }
}
