package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.models.AppUser;
import org.example.models.ApiResponse;
import org.example.models.dto.UserPublicDto;
import org.example.models.dto.UserCreateDto;
import org.example.services.persistance.UserDbService;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDbService userDbService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserDbService userDbService, PasswordEncoder passwordEncoder) {
        this.userDbService = userDbService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateDto user) {
        if (!user.isValid()){
           return user.badRequestResponse();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser newUser = AppUser.fromDto(user);
        AppUser savedUser = userDbService.createUser(newUser);

        LuaModManager luaManager = LuaModManager.getInstance();
        LuaTable safeUser = new LuaTable();
        safeUser.set("id", savedUser.getId());
        safeUser.set("username", savedUser.getUsername());
        safeUser.set("role", savedUser.getRole());
        luaManager.triggerEvent("onUserCreate", safeUser);

        return user.successResponse(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicDto> getUser(@PathVariable Long id) {

        Optional<UserPublicDto> maybeUser = userDbService.getUserById(id)
                .map(UserPublicDto::fromAppUser);

        if (maybeUser.isPresent()) {
            UserPublicDto savedUser = maybeUser.get();
            LuaModManager luaManager = LuaModManager.getInstance();
            LuaTable safeUser = new LuaTable();
            safeUser.set("id", savedUser.getId());
            safeUser.set("username", savedUser.getUsername());
            safeUser.set("email", savedUser.getEmail());
            safeUser.set("timestamp", String.valueOf(savedUser.getTimestamp()));
            luaManager.triggerEvent("onGetUserByID", safeUser);
            return ResponseEntity.ok(savedUser);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userDbService.getAllUsers());
    }


    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(
            @PathVariable Long id,
            @RequestBody AppUser userUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AppUser existing = userDbService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existing.setUsername(userUpdate.getUsername());
        existing.setEmail(userUpdate.getEmail());

        if (userUpdate.getPassword() != null && !userUpdate.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }

        LuaModManager luaManager = LuaModManager.getInstance();
        LuaTable safeUser = new LuaTable();
        safeUser.set("id", existing.getId());
        safeUser.set("username", existing.getUsername());
        safeUser.set("role", existing.getRole());
        luaManager.triggerEvent("onUpdateUserById", safeUser);

        return ResponseEntity.ok(userDbService.saveUser(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!currentUser.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userDbService.deleteUserById(id);


        LuaModManager luaManager = LuaModManager.getInstance();
        LuaTable safeUser = new LuaTable();
        luaManager.triggerEvent("onDeleteUserById", safeUser);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unique")
    public ResponseEntity<List<UserPublicDto>> getUnique(
        @RequestParam int limit,
        @RequestParam int offset
    ){
        List<UserPublicDto> userList = UserPublicDto.fromAppUserList(userDbService.getUserUnique(limit, (offset * limit)));

        LuaModManager luaManager = LuaModManager.getInstance();
        LuaTable safeUser = new LuaTable();
        safeUser.set("userList", (LuaValue) userList);
        luaManager.triggerEvent("onGetUniqueUsers", safeUser);

        return ResponseEntity.ok(userList);
    }
}
