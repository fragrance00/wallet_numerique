package com.wallet.backend.config;

import com.wallet.backend.entities.Banker;
import com.wallet.backend.repository.BankerRepository;
import com.wallet.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BankerInitializer implements CommandLineRunner {

    @Autowired
    private BankerRepository bankerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Maintenant stocke en clair

    @Autowired
    private JwtService jwtService;

    @Value("${banker.default.username:superadmin}")
    private String bankerUsername;

    @Value("${banker.default.password:admin123}")
    private String bankerPassword;

    @Value("${banker.default.email:superadmin@wallet.com}")
    private String bankerEmail;

    @Override
    public void run(String... args) {
        Banker banker = bankerRepository.findByUsername(bankerUsername).orElse(null);

        if (banker == null) {
            banker = Banker.builder()
                    .firstName("Super")
                    .lastName("Admin")
                    .email(bankerEmail)
                    .username(bankerUsername)
                    .password(bankerPassword) // ⚠️ Maintenant en clair (pas de encode())
                    .role("SUPER_ADMIN")
                    .build();

            banker = bankerRepository.save(banker);
            System.out.println("✅ Default SUPER_ADMIN banker created - Username: " + bankerUsername + " | Password: " + bankerPassword);
        } else {
            System.out.println("ℹ️ Banker account already exists");
        }

        // Générer et afficher le token JWT
        String token = jwtService.generateToken(banker.getUsername(), banker.getRole(), banker.getId());

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🚀 SUPER ADMIN TOKEN GENERATED AUTOMATICALLY");
        System.out.println("=".repeat(80));
        System.out.println("Token: " + token);
        System.out.println("Username: " + bankerUsername);
        System.out.println("Password: " + bankerPassword);
        System.out.println("Role: SUPER_ADMIN");
        System.out.println("Use this token to access protected endpoints:");
        System.out.println("GET http://localhost:8080/api/bankers");
        System.out.println("Header: Authorization: Bearer " + token);
        System.out.println("=".repeat(80) + "\n");
    }
}