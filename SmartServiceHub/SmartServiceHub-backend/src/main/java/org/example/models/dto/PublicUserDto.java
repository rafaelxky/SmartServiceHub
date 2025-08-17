package org.example.models.dto;

import org.example.models.AppUser;

import java.util.ArrayList;
import java.util.List;

public class PublicUserDto {
    public String username;

    public PublicUserDto(String username) {
        this.username = username;
    }

    public static PublicUserDto fromAppUser(AppUser appUser){
        return new PublicUserDto(appUser.getUsername());
    }

    public static List<PublicUserDto> fromAppUserList(List<AppUser> userList){
        List<PublicUserDto> publicUserList = new ArrayList<>();

        for (AppUser user : userList){
            publicUserList.add(PublicUserDto.fromAppUser(user));
        }

        return publicUserList;
    }
}
