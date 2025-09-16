package org.example.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvSetup {
    public static void setupDbEnv(){
        Dotenv dotenv = Dotenv.configure()
                .filename("db.env")
                .load();

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("TEST_DB_URL", dotenv.get("DB_URL"));
        System.setProperty("TEST_DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("TEST_DB_PASSWORD", dotenv.get("DB_PASSWORD"));
    }
}
