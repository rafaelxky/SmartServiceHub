package org.example.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.models.AppUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserPublicDto {
    public Long id;
    public String username;
    public String email;
    public LocalDateTime timestamp;

    public static UserPublicDto fromAppUser(AppUser appUser){
        return new UserPublicDto(appUser.getId(), appUser.getUsername() ,appUser.getEmail(), appUser.getTimestamp());
    }

    public static List<UserPublicDto> fromAppUserList(List<AppUser> userList){
        List<UserPublicDto> publicUserList = new ArrayList<>();

        for (AppUser user : userList){
            publicUserList.add(UserPublicDto.fromAppUser(user));
        }

        return publicUserList;
    }
}
