package org.example.lua;

import org.bytedeco.jnlua.LuaState;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class LuaStartup {

    protected static final String SCRIPTS_PATH = "../scripts";

    @PostConstruct
    public void init() {
        LuaModManager luaManager = LuaModManager.getInstance();

        String folderPath = new File(SCRIPTS_PATH).getAbsolutePath();

        System.out.println("Lua init started");

        luaManager.loadAllScriptsFromFolder(folderPath);
        luaManager.watchLuaFolder(folderPath);
        luaManager.triggerEvent("onAppStartup", null);

        System.out.println("Lua initialization complete");
    }
}
