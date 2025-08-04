package org.example.controllers;

import org.example.models.AppUser;
import org.example.services.persistance.UserDbService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDbService userDbService;

    public UserController(UserDbService userDbService) {
        this.userDbService = userDbService;
    }

    @PostMapping
    public AppUser createUser(@RequestBody AppUser user) {
        return userDbService.saveUser(user);
    }

    @GetMapping("/{id}")
    public Optional<AppUser> getUser(@PathVariable Long id) {
        return userDbService.getUserById(id);
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return userDbService.getAllUsers();
    }

    @PutMapping("/{id}")
    public AppUser updateUser(@PathVariable Long id, @RequestBody AppUser user) {
        user.setId(id);
        return userDbService.saveUser(user);
    }

    @DeleteMapping("/{id}")

    public void deleteUser(@PathVariable Long id) {
        userDbService.deleteUserById(id);
    }
}
