package com.example.rbac.controller;

import com.example.rbac.entity.User;
import com.example.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for super admin operations.
 * Requires SUPER_ADMIN role for all endpoints.
 * Provides full access to user management.
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users in the system.
     * @return list of all users
     */
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Deletes any user by username.
     * @param username the username of the user to delete
     * @return success message
     */
    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(user.getId());
        return ResponseEntity.ok("User deleted successfully");
    }
}
