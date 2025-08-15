package org.example.models;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    MODERATOR("ROLE_MODERATOR");

    private final String roleName;

    Roles(String roleName){
        this.roleName = roleName;
    }

    @Override
    public String toString(){
        return roleName;
    }

}


