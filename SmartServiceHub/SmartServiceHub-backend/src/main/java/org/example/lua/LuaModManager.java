package org.example.lua;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.*;
import java.nio.file.*;
import java.util.*;

// Manages Lua event hooks using an observer pattern; supports hot reloading Lua scripts.
public class LuaModManager {

    private static LuaModManager instance;

    private final Globals globals;

    private final Map<String, Map<String, List<LuaFunction>>> scriptEventHooks = new HashMap<>();

    private final String SCRIPTS_PATH = LuaStartup.SCRIPTS_PATH;

    private LuaModManager() {
        globals = JsePlatform.standardGlobals();

        globals.set("register_event", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                LuaTable table = arg.checktable();
                String eventName = table.get("event").checkjstring();
                LuaFunction func = table.get("func").checkfunction();


                // Determine which Lua script is currently registering the event
                String currentScript = globals.get("CURRENT_SCRIPT").isstring() ?
                        globals.get("CURRENT_SCRIPT").tojstring() : "unknown";

                scriptEventHooks
                        .computeIfAbsent(currentScript, k -> new HashMap<>())
                        .computeIfAbsent(eventName, k -> new ArrayList<>())
                        .add(func);
                return LuaValue.NIL;
            }
        });


        globals.set("reload", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String file = arg.checkjstring();
                runLuaScriptFromFile(SCRIPTS_PATH + "/" + file);
                return LuaValue.NIL;
            }
        });
    }

    public static LuaModManager getInstance() {
        if (instance == null) instance = new LuaModManager();
        return instance;
    }

    public void runLuaScriptFromFile(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            scriptEventHooks.remove(filePath);

            globals.set("CURRENT_SCRIPT", LuaValue.valueOf(filePath));

            globals.load(reader, filePath).call();
            System.out.println("Lua script loaded: " + filePath);
        } catch (Exception e) {
            System.err.println("Failed to load Lua script: " + filePath + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadAllScriptsFromFolder(String folderPath) {
        File dir = new File(folderPath);
        if (!dir.exists()) return;

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.getName().endsWith(".lua")) {
                runLuaScriptFromFile(file.getAbsolutePath());
            }
        }
    }

    public void triggerEvent(String eventName, LuaTable data) {
        for (Map.Entry<String, Map<String, List<LuaFunction>>> scriptEntry : scriptEventHooks.entrySet()) {
            Map<String, List<LuaFunction>> hooks = scriptEntry.getValue();
            List<LuaFunction> funcs = hooks.get(eventName);
            if (funcs != null) {
                for (LuaFunction func : funcs) {
                    try {
                        func.call(data != null ? data : LuaValue.NIL);
                    } catch (LuaError e) {
                        System.err.println("Lua script error in event '" + eventName + "' (" + scriptEntry.getKey() + "): " + e.getMessage());
                    }
                }
            }
        }
    }

    public Globals getGlobals() {
        return globals;
    }

    // Watches Lua folder and hot-reloads scripts on changes
    public void watchLuaFolder(String folderPath) {
        new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(folderPath);
                path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

                System.out.println("Watching folder for Lua changes: " + folderPath);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        String fileName = event.context().toString();
                        if (fileName.endsWith(".lua")) {
                            String fullPath = path.resolve(fileName).toString();
                            System.out.println("Detected modification: " + fullPath);
                            runLuaScriptFromFile(fullPath);
                        }
                    }
                    key.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}