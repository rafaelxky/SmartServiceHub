package org.example;

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

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onAppStartup", new LuaTable());

        SpringApplication.run(SmartServiceHub.class, args);
    }
}

