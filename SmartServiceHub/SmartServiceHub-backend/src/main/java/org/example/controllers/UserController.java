package org.example.controllers;

import org.example.models.AppUser;
import org.example.models.Roles;
import org.example.models.dto.ApiResponse;
import org.example.models.dto.UserCreateDto;
import org.example.services.persistance.UserDbService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

        if (user.getPassword().isEmpty() || user.getUsername().isEmpty() || user.getEmail().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, """ 
                    Error, bad request. A user must be created like {"username": "exampleName", "email": "example@example.com", "password": "example123" }
                    """, null));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser newUser = AppUser.fromDto(user);
        AppUser savedUser = userDbService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "User created successfully", savedUser));
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getUSer(@PathVariable Long id){
        return userDbService.getUserById(id)
                .map(user -> ResponseEntity.ok(user.getUsername()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUser(@PathVariable Long id) {
        return userDbService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole(T(com.example.models.Roles).ADMIN)")
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
        return ResponseEntity.noContent().build();
    }
}
