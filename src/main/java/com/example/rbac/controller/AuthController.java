package com.example.rbac.controller;

import com.example.rbac.dto.LoginRequestDto;
import com.example.rbac.dto.LoginResponseDto;
import com.example.rbac.dto.RegisterRequestDto;
import com.example.rbac.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller handling authentication-related endpoints.
 * Provides endpoints for user registration and login.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Registers a new user.
     * @param request the registration details
     * @return success message or error
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequestDto request) {

        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    /**
     * Authenticates a user and returns JWT token.
     * @param request login credentials
     * @return LoginResponseDto with token and role
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }

    /**
     * Authenticates a super admin user.
     * @param request login credentials
     * @return LoginResponseDto if super admin
     */
    @PostMapping("/super-admin-login")
    public LoginResponseDto superAdminLogin(@RequestBody LoginRequestDto request) {
        return authService.superAdminLogin(request);
    }
}
