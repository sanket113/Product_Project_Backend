package com.example.rbac.config;

import com.example.rbac.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component that runs on application startup to initialize data.
 * Creates the default super admin user if it doesn't exist.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        authService.createSuperAdmin();
    }
}
