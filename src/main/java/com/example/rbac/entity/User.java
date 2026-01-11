package com.example.rbac.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a User in the RBAC system.
 * This class maps to the "users" table in the database.
 */
@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    /**
     * Unique identifier for the user, generated as a UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Unique username for authentication.
     */
    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;
    /**
     * Hashed password for security.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Role of the user: "USER", "MANAGER", or "SUPER_ADMIN".
     */
    @Column(nullable = false)
    private String role; // "USER", "MANAGER", "SUPER_ADMIN"
}
