package com.example.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {

    ADMIN,
    USER;

    public boolean isUser() {
        return this.equals(USER);
    }
}
