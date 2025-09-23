package org.example;

import org.example.config.EnvSetup;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestEnvSetup {
    static {
        EnvSetup.setupDbEnv();
    }
}
