package org.example.models;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("ADMIN"),
    USER("USER"),
    MODERATOR("MODERATOR");

    public final String roleName;

    Roles(String roleName){
        this.roleName = roleName;
    }

    @Override
    public String toString(){
        return roleName;
    }

}


