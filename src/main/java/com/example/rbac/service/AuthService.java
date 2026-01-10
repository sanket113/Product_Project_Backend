package com.example.rbac.service;

import com.example.rbac.dto.LoginRequestDto;
import com.example.rbac.dto.LoginResponseDto;
import com.example.rbac.dto.RegisterRequestDto;
import com.example.rbac.entity.User;
import com.example.rbac.repository.UserRepository;
import com.example.rbac.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class handling authentication and user registration logic.
 * Provides methods for user registration, login, and super admin creation.
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with the provided details.
     * Validates username uniqueness and role validity.
     * @param request the registration request containing username, email, password, and role
     * @throws RuntimeException if email already exists or role is invalid
     */
    // 🔹 REGISTER
    public void register(RegisterRequestDto request) {

        // check duplicate user
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // ✅ HASH PASSWORD
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // ✅ ASSIGN ROLE
        String requestedRole = request.getRole();
        if (requestedRole == null || requestedRole.isEmpty()) {
            user.setRole("USER"); // Default
        } else {
            String roleUpper = requestedRole.toUpperCase();
            if (!roleUpper.equals("USER") && !roleUpper.equals("MANAGER")) {
                throw new RuntimeException("Invalid role. Allowed: USER, MANAGER");
            }
            user.setRole(roleUpper);
        }

        userRepository.save(user);
    }

    /**
     * Authenticates a user and returns a JWT token with role.
     * @param request the login request containing username and password
     * @return LoginResponseDto containing token and role
     */
    // 🔹 LOGIN
    public LoginResponseDto login(LoginRequestDto request) {
        return authenticateAndGetToken(request.getEmail(), request.getPassword());
    }

    /**
     * Authenticates a super admin user.
     * @param request the login request
     * @return LoginResponseDto if user is super admin
     * @throws RuntimeException if not a super admin
     */
    // 🔹 SUPER ADMIN LOGIN
    public LoginResponseDto superAdminLogin(LoginRequestDto request) {
        LoginResponseDto loginResponse = authenticateAndGetToken(request.getEmail(), request.getPassword());
        if (!"ROLE_SUPER_ADMIN".equals(loginResponse.getRole())) {
            throw new RuntimeException("Access Denied: Not a Super Admin");
        }
        return loginResponse;
    }

    /**
     * Performs authentication and generates JWT token.
     * @param email the username
     * @param password the password
     * @return LoginResponseDto with token and role
     */
    private LoginResponseDto authenticateAndGetToken(String email, String password) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        UserDetails user = (UserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(user);
        String role = user.getAuthorities().iterator().next().getAuthority();

        return new LoginResponseDto(token, role);
    }

    /**
     * Creates or updates the super admin user.
     * Used for initialization purposes.
     */
    // 🔹 CREATE SUPER ADMIN (Internal use)
    public void createSuperAdmin() {
        User admin = userRepository.findByEmail("admin@gmail.com").orElse(new User());
        admin.setUsername("superadmin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("SUPER_ADMIN");
        userRepository.save(admin);
        System.out.println("✅ Super Admin updated/created: superadmin / admin123");
    }
}
