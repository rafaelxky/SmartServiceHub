package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.lua.LuaTableAdaptor;
import org.example.models.AppUser;
import org.example.models.dto.UserPublicDto;
import org.example.models.dto.UserCreateDto;
import org.example.models.responses_requests.GenericErrorResponse;
import org.example.models.responses_requests.GenericSuccessResponse;
import org.example.services.persistance.UserDbService;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    // todo: pass the proper tables to lua

    private final UserDbService userDbService;
    private final PasswordEncoder passwordEncoder;
    private final LuaModManager luaManager;

    public UserController(UserDbService userDbService, PasswordEncoder passwordEncoder, LuaModManager luaManager) {
        this.userDbService = userDbService;
        this.passwordEncoder = passwordEncoder;
        this.luaManager = luaManager;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody UserCreateDto user) {
        if (!user.isValid()){
           return user.badRequestResponse();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser newUser = AppUser.fromDto(user);
        AppUser savedUser = userDbService.createUser(newUser);

        luaManager.triggerEvent("onUserCreate", LuaTableAdaptor.fromAppUser(savedUser));

        return user.successResponse(savedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserByID(@PathVariable Long id) {

        Optional<UserPublicDto> maybeUser = userDbService.getUserById(id)
                .map(UserPublicDto::fromAppUser);

        if (maybeUser.isPresent()) {
            UserPublicDto savedUser = maybeUser.get();
            luaManager.triggerEvent("onGetUserById", LuaTableAdaptor.fromUserPublicDto(savedUser));
            return ResponseEntity.ok(savedUser);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericSuccessResponse("User %d not found".formatted(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(
            @PathVariable Long id,
            @RequestBody AppUser userUpdate,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse("User not logged in"));
        }

        if (!Objects.equals(currentUser.getId(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericErrorResponse("Logged user cannot modify this other user"));
        }

        AppUser existing = userDbService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existing.setUsername(userUpdate.getUsername());
        existing.setEmail(userUpdate.getEmail());

        if (userUpdate.getPassword() != null && !userUpdate.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        }

        luaManager.triggerEvent("onUpdateUserById", LuaTableAdaptor.fromAppUser(existing));

        return ResponseEntity.ok(userDbService.saveUser(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal AppUser currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new GenericErrorResponse("User not logged in!"));
        }

        if (!Objects.equals(currentUser.getId(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericErrorResponse("Logged user cannot modify other user or does not have enough permissions!"));
        }

        userDbService.deleteUserById(id);


        luaManager.triggerEvent("onDeleteUserById", null);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericSuccessResponse("User %d deleted successfully".formatted(id)));
    }

    @GetMapping("/unique")
    public ResponseEntity<Object> getUnique(
        @RequestParam int limit,
        @RequestParam int offset
    ){
        List<UserPublicDto> userList = UserPublicDto.fromAppUserList(userDbService.getUserUnique(limit, (offset * limit)));

        LuaTable safeUser = new LuaTable();
        safeUser.set("userList", CoerceJavaToLua.coerce(userList.stream().map(LuaTableAdaptor::fromUserPublicDto)));
        luaManager.triggerEvent("onGetUniqueUsers", safeUser);

        return ResponseEntity.ok(userList);
    }
}
