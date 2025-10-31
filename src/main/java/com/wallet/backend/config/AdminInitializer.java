package com.wallet.backend.config;

import com.wallet.backend.entities.Admin;
import com.wallet.backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.default.username:admin}")
    private String adminUsername;

    @Value("${admin.default.password:admin123}")
    private String adminPassword;

    @Value("${admin.default.email:admin@wallet.com}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        if (!adminRepository.existsByUsername(adminUsername)) {
            Admin admin = Admin.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .email(adminEmail)
                    .username(adminUsername)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .build();
            adminRepository.save(admin);
            System.out.println("Default admin account created - Username: " + adminUsername + " | Password: " + adminPassword);
        }
    }
}