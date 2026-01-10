package com.example.rbac.controller;

import com.example.rbac.entity.User;
import com.example.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for user-specific operations.
 * Handles profile retrieval for authenticated users.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves the profile of the currently authenticated user.
     * @return User object containing user details
     */
//    @GetMapping("/profile")
//    public User getProfile() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getE();
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
}
