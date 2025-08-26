package org.example.controllers;

import org.example.lua.LuaModManager;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import java.util.stream.Stream;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {
// todo: pass the correct data to lua tables

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @GetMapping("/home")
    @ResponseBody
    public String listAllEndpoints() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info("Home");

        Set<RequestMappingInfo> mappings = handlerMapping.getHandlerMethods().keySet();

        String endpoints = mappings.stream()
                .flatMap(mapping -> {
                    if (mapping.getPatternsCondition() != null) {
                        return mapping.getPatternsCondition().getPatterns().stream();
                    } else if (mapping.getPathPatternsCondition() != null) {
                        return mapping.getPathPatternsCondition().getPatterns().stream()
                                .map(Object::toString);
                    } else {
                        return Stream.<String>empty();
                    }
                })
                
                .sorted()
                .collect(Collectors.joining("<br>"));

        String[] endpointsArr = endpoints.split(" ");
        LuaTable luaTable = new LuaTable();
        for (int i = 0; i < endpointsArr.length; i++) {
            luaTable.set(i + 1, endpointsArr[i]);
        }

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onHomeAccessed", luaTable);

        return endpoints;
    }

    @GetMapping("/status")
    @ResponseBody
    public String getServerStatus(){
        return "up";
    }

}