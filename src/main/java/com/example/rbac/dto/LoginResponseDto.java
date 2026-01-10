package com.example.rbac.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for login responses.
 * Contains the JWT token and user role after successful authentication.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
    /**
     * The JWT token for authenticated requests.
     */
    private String token;

    /**
     * The role of the authenticated user (e.g., "ROLE_USER").
     */
    private String role;
}
