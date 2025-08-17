package org.example.controllers;

import org.example.models.AppUser;
import org.example.models.Roles;
import org.example.models.dto.ApiResponse;
import org.example.models.dto.UserCreateDto;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createAdmin(@RequestBody UserCreateDto user) {
        if (user.isValid()){
            return user.badRequestResponse();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        AppUser newUser = AppUser.fromDto(user);
        newUser.setRole(Roles.ADMIN.getRoleName());
        AppUser savedUser = userRepository.save(newUser);
        return user.successResponse(savedUser);

    }

    @PostMapping("/users/deleteAll")
    public ResponseEntity<String> deleteAllUsers(
            @RequestParam(required = false) String confirm
    ){
        if (!confirm.equals("DELETE_ALL_USERS")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Confirmation phrase required: DELETE_ALL_USERS. (...?confirm=DELETE_ALL_USERS)");
        }
        userRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("All users have been deleted");
    }



    @PostMapping("/comments/deleteAll")
    public ResponseEntity<String> deleteAllComments(
            @RequestParam(required = false) String confirm
    ){
        if (!confirm.equals("DELETE_ALL_COMMENTS")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Confirmation phrase required: DELETE_ALL_COMMENTS. (...?confirm=DELETE_ALL_COMMENTS)");
        }
        userRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("All comments have been deleted");
    }

    @PostMapping("/posts/deleteAll")
    public ResponseEntity<String> deleteAllPosts(
            @RequestParam(required = false) String confirm
    ){
        if (!confirm.equals("DELETE_ALL_POSTS")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Confirmation phrase required: DELETE_ALL_POSTS. (...?confirm=DELETE_ALL_POSTS)");
        }
        userRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).body("All posts have been deleted");
    }
}
