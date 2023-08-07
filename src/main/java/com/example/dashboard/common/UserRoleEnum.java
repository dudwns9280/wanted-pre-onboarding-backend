package com.example.dashboard.common;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRoleEnum {
    USER("user");

    private final String roleType;

    public String getRoleType() {
        return this.roleType;
    }

}