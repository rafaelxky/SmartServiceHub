package org.example.controllers;

import org.example.models.AppUser;
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
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser savedUser = userDbService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
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
