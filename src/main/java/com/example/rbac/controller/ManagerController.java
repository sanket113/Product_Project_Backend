package com.example.rbac.controller;

import com.example.rbac.entity.User;
import com.example.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for manager-specific operations.
 * Requires MANAGER role for all endpoints.
 * Allows managers to view and delete regular users.
 */
@RestController
@RequestMapping("/manager")
@PreAuthorize("hasRole('MANAGER')")
public class ManagerController {

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves all users with USER role.
     * @return list of regular users
     */
    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findByRole("USER");
    }

    /**
     * Deletes a user by username, but only if they have USER role.
     * @param username the username of the user to delete
     * @return success or error response
     */
    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if ("USER".equals(user.getRole())) {
                userRepository.deleteById(user.getId());
                return ResponseEntity.ok("User deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Managers can only delete USER roles");
            }
        }
        return ResponseEntity.notFound().build();
    }
}
