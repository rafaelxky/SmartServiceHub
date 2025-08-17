package org.example.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.models.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
@Getter
@Setter
public class UserCreateDto {
    public String username;
    public String email;
    public String password;

    public boolean isValid(){
        return !(
                username == null || email == null || password == null ||
                username.isBlank() || email.isEmpty() || password.isEmpty()
        );
    }

    public ResponseEntity<ApiResponse> badRequestResponse(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, """ 
                    Error, bad request. A user must be created like {"username": "exampleName", "email": "example@example.com", "password": "example123" }
                    """, null));
    }

    public ResponseEntity<ApiResponse> successResponse(AppUser savedUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "User created successfully", savedUser));
    }
}
