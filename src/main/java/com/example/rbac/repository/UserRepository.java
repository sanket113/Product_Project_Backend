package com.example.rbac.repository;

import com.example.rbac.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 * Extends JpaRepository to provide CRUD operations and custom query methods.
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Finds a user by their email.
     * @param email the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds all users with a specific role.
     * @param role the role to filter by
     * @return list of users with the specified role
     */
    List<User> findByRole(String role);
}
