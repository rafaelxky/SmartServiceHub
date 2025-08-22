package org.example.lua;

import org.luaj.vm2.LuaTable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class LuaStartup {

    @PostConstruct
    public void init() {
        LuaModManager luaManager = LuaModManager.getInstance();

        System.out.println("Lua init executed");
        System.out.println("Lua load script started");

        // Load main Lua script (must be at src/main/resources/lua/Main.lua)
        luaManager.runLuaScriptFromClasspath("lua/Main.lua");
        luaManager.triggerEvent("onHomeAccessed", new LuaTable());

        System.out.println("Lua initialization complete");
    }
}
