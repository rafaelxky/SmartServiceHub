package org.example.lua;

import org.bytedeco.jnlua.LuaState;
import org.bytedeco.jnlua.LuaStateFactory;

import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LuaModManager {

    private static LuaModManager instance;

    private final LuaState lua;
    private final Map<String, Map<String, List<Integer>>> scriptEventHooks = new ConcurrentHashMap<>();

    private LuaModManager() {
        lua = LuaStateFactory.newLuaState();
        lua.openLibs(); // load standard Lua libraries including C modules

        instance = this;
    }

    public static LuaModManager getInstance() {
        if (instance == null) instance = new LuaModManager();
        return instance;
    }

    public LuaState getLuaState() {
        return lua;
    }

    public void runLuaScriptFromFile(String filePath) {
        try {
            // clear previous hooks for this script
            scriptEventHooks.remove(filePath);

            // load and execute Lua file
            lua.LdoFile(filePath);

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

    public void triggerEvent(String eventName, Map<String, Object> data) {
        for (Map.Entry<String, Map<String, List<Integer>>> scriptEntry : scriptEventHooks.entrySet()) {
            Map<String, List<Integer>> hooks = scriptEntry.getValue();
            List<Integer> funcRefs = hooks.get(eventName);
            if (funcRefs != null) {
                for (Integer ref : funcRefs) {
                    lua.rawGet(LuaState.REGISTRYINDEX, ref); // push Lua function
                    if (data != null) {
                        pushMapAsLuaTable(lua, data); // push argument table
                        lua.call(1, 0);
                    } else {
                        lua.call(0, 0);
                    }
                }
            }
        }
    }

    private void pushMapAsLuaTable(LuaState lua, Map<String, Object> map) {
        lua.newTable();
        map.forEach((key, value) -> {
            lua.pushString(key);
            if (value instanceof Number) lua.pushNumber(((Number) value).doubleValue());
            else if (value instanceof Boolean) lua.pushBoolean((Boolean) value);
            else lua.pushString(value.toString());
            lua.setTable(-3);
        });
    }

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
