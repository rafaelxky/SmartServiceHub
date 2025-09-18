package org.example.controllers;

import org.example.lua.LuaModManager;
import org.example.lua.LuaTableAdaptor;
import org.example.models.AppUser;
import org.example.models.Roles;
import org.example.models.dto.UserCreateDto;
import org.example.models.responses_requests.GenericErrorResponse;
import org.example.models.responses_requests.GenericSuccessResponse;
import org.example.services.persistance.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    // todo: pass the proper tables to lua

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createAdmin(@RequestBody UserCreateDto user) {
        if (user.isValid()){
            return user.badRequestResponse();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser newUser = AppUser.fromDto(user);
        newUser.setRole(Roles.ADMIN.getRoleName());
        AppUser savedUser = userRepository.save(newUser);

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onAdminCreate", LuaTableAdaptor.fromAppUser(savedUser));

        return user.successResponse(savedUser);

    }

    @PostMapping("/users/deleteAll")
    public ResponseEntity<Object> deleteAllUsers(
            @RequestParam(required = false) String confirm
    ){
        if (!confirm.equals("DELETE_ALL_USERS")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponse("Confirmation phrase required: DELETE_ALL_USERS. (...?confirm=DELETE_ALL_USERS)"));
        }
        userRepository.deleteAll();

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onDeleteAllUsers", null);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericSuccessResponse("All user have been successfully deleted!"));
    }

    @PostMapping("/comments/deleteAll")
    public ResponseEntity<Object> deleteAllComments(
            @RequestParam(required = false) String confirm
    ){
        if (!confirm.equals("DELETE_ALL_COMMENTS")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponse("Confirmation phrase required: DELETE_ALL_COMMENTS. (...?confirm=DELETE_ALL_COMMENTS)"));
        }
        userRepository.deleteAll();

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onDeleteAllComments", null);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericSuccessResponse("All comments have been successfully deleted!"));
    }

    @PostMapping("/posts/deleteAll")
    public ResponseEntity<Object> deleteAllPosts(
            @RequestParam(required = false) String confirm
    ){
        if (!confirm.equals("DELETE_ALL_POSTS")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponse("Confirmation phrase required: DELETE_ALL_POSTS. (...?confirm=DELETE_ALL_POSTS)"));
        }
        userRepository.deleteAll();

        LuaModManager luaManager = LuaModManager.getInstance();
        luaManager.triggerEvent("onDeleteAllPosts", null);

        return ResponseEntity.status(HttpStatus.OK).body(new GenericSuccessResponse("All posts have been successfully deleted!"));
    }
}
