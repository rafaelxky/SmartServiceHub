package org.example.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.models.AppUser;
import org.example.models.responses_requests.GenericErrorResponse;
import org.example.models.responses_requests.GenericSuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@Setter
public class UserCreateDto {
    @NotBlank(message = "username is required ('username' = 'examplename')")
    public String username;
    @NotBlank(message = "email is required ('email' = 'email@example.com')")
    public String email;
    @NotBlank(message = "password is required ('password' = 'example123')")
    public String password;

    public boolean isValid(){
        return !(
                username == null || email == null || password == null ||
                username.isBlank() || email.isEmpty() || password.isEmpty()
        );
    }

    public ResponseEntity<Object> badRequestResponse(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GenericErrorResponse("Incorrect creation parameters: username, email, passowrd"));
    }

    public ResponseEntity<Object> successResponse(AppUser savedUser){
        return ResponseEntity.status(HttpStatus.CREATED).body((new GenericSuccessResponse("User created successfully!")));
    }
}
