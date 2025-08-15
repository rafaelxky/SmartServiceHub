package org.example.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserCreateDto {
    private String username;
    private String email;
    private String password;
}
