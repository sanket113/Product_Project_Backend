package com.example.rbac.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for user registration requests.
 * Contains the information needed to create a new user account.
 */
@Getter
@Setter
public class RegisterRequestDto {
    /**
     * The desired username for the new account.
     */
    private String username;

    /**
     * The password for the new account.
     */
    private String password;

    /**
     * The role to assign to the new user (optional, defaults to "USER").
     */
    private String role;
}
