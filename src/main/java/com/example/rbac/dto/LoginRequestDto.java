package com.example.rbac.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for login requests.
 * Contains the credentials needed for user authentication.
 */
@Getter
@Setter
public class LoginRequestDto {
    /**
     * The username of the user attempting to log in.
     */
    private String email;

    /**
     * The password of the user.
     */
    private String password;
}
