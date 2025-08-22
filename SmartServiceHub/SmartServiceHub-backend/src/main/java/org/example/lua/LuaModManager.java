package org.example.lua;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStreamReader;
import java.util.*;

public class LuaModManager {

    private static LuaModManager instance;

    private final Globals globals;
    private final Map<String, List<LuaFunction>> eventHooks = new HashMap<>();

    private LuaModManager() {
        globals = JsePlatform.standardGlobals();

        globals.set("register_event", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                LuaTable table = arg.checktable();
                String eventName = table.get("event").checkjstring();
                LuaFunction func = table.get("func").checkfunction();

                eventHooks.computeIfAbsent(eventName, k -> new ArrayList<>()).add(func);
                System.out.println("Registered Lua hook for event: " + eventName);
                return LuaValue.NIL;
            }
        });
    }

    public static LuaModManager getInstance() {
        if (instance == null) instance = new LuaModManager();
        return instance;
    }

    public void runLuaScriptFromClasspath(String path) {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))) {
            globals.load(reader, path).call();
            System.out.println("Lua script loaded: " + path);
        } catch (Exception e) {
            System.err.println("Failed to load Lua script: " + path + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadAllScriptsFromFolder(String folder) {
        try {
            ClassPathResource resource = new ClassPathResource(folder);
            for (String fileName : Objects.requireNonNull(resource.getFile().list())) {
                if (fileName.endsWith(".lua")) {
                    runLuaScriptFromClasspath(folder + "/" + fileName);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load Lua scripts from folder: " + folder + " : " + e.getMessage());
        }
    }

    public void triggerEvent(String eventName, LuaTable data) {
        List<LuaFunction> hooks = eventHooks.get(eventName);
        if (hooks != null) {
            for (LuaFunction func : hooks) {
                try {
                    func.call(data != null ? data : LuaValue.NIL);
                } catch (LuaError e) {
                    System.err.println("Lua script error in event '" + eventName + "': " + e.getMessage());
                }
            }
        }
    }

    public Globals getGlobals() {
        return globals;
    }
}
