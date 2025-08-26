package org.example.lua;

import org.example.models.ServicePost;
import org.example.models.AppUser;
import org.example.models.dto.UserPublicDto;
import org.luaj.vm2.LuaTable;

public class LuaTableAdaptor {
    public static LuaTable fromAppServiceCreateDto(ServicePost servicePost){
        LuaTable safeAppService = new LuaTable();
        safeAppService.set("id", servicePost.getId());
        safeAppService.set("title", servicePost.getTitle());
        safeAppService.set("content", servicePost.getContent());
        safeAppService.set("creatorId", servicePost.getCreatorId());
        safeAppService.set("timestamp", String.valueOf(servicePost.getTimestamp()));
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

    public static LuaTable fromServicePost(ServicePost servicePost) {
        LuaTable safeServicePost = new LuaTable();
        safeServicePost.set("id", servicePost.getId());
        safeServicePost.set("title", servicePost.getTitle());
        safeServicePost.set("content", servicePost.getContent());
        safeServicePost.set("creatorId", servicePost.getCreatorId());
        safeServicePost.set("timestamp", String.valueOf(servicePost.getTimestamp()));
        return safeServicePost;
    }
}
