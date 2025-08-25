package org.example.lua;

import org.example.models.AppService;
import org.example.models.AppUser;
import org.example.models.dto.UserPublicDto;
import org.luaj.vm2.LuaTable;

public class LuaTableAdaptor {
    public static LuaTable fromAppServiceCreateDto(AppService appService){
        LuaTable safeAppService = new LuaTable();
        safeAppService.set("id", appService.getId());
        safeAppService.set("title", appService.getTitle());
        safeAppService.set("content", appService.getContent());
        safeAppService.set("userId", appService.getCreatorId());
        safeAppService.set("timestamp", String.valueOf(appService.getTimestamp()));
        return safeAppService;
    }

    public static LuaTable fromAppUser(AppUser appUser){
        LuaTable safeUser = new LuaTable();
        safeUser.set("id", appUser.getId());
        safeUser.set("username", appUser.getUsername());
        safeUser.set("role", appUser.getRole());
        return safeUser;
    }

    public static LuaTable fromUserPublicDto(UserPublicDto savedUser) {
        LuaTable safeUser = new LuaTable();
        safeUser.set("id", savedUser.getId());
        safeUser.set("username", savedUser.getUsername());
        safeUser.set("email", savedUser.getEmail());
        safeUser.set("timestamp", String.valueOf(savedUser.getTimestamp()));
        return safeUser;
    }
}
