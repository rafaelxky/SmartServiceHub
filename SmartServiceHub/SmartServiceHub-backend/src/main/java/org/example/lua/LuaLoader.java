package org.example.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.io.InputStreamReader;

public class LuaLoader {

    private Globals globals = JsePlatform.standardGlobals();

    public void loadScript(String luaFileName) {
        try {
            ClassPathResource resource = new ClassPathResource("lua/" + luaFileName);
            InputStream inputStream = resource.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            globals.load(reader, luaFileName).call();

            reader.close();
            inputStream.close();

            System.out.println(luaFileName + " loaded successfully.");
        } catch (Exception e) {
            System.err.println("Failed to load Lua script: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Globals getGlobals() {
        return globals;
    }
}
