package org.example.lua;

import org.luaj.vm2.LuaTable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class LuaStartup {
    protected static final String SCRIPTS_PATH = "../scripts";

    @PostConstruct
    public void init() {
        String folderPath = new File(LuaStartup.SCRIPTS_PATH).getAbsolutePath();

        LuaModManager luaManager = LuaModManager.getInstance();

        System.out.println("Lua init started");

        luaManager.loadAllScriptsFromFolder(folderPath);
        luaManager.watchLuaFolder(folderPath);
        luaManager.triggerEvent("onAppStartup", new LuaTable());

        System.out.println("Lua initialization complete");
    }
}