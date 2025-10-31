package com.wallet.backend.config;

import com.wallet.backend.entities.Banker;
import com.wallet.backend.repository.BankerRepository;
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
    private PasswordEncoder passwordEncoder;

    @Value("${banker.default.username:banker}")
    private String bankerUsername;

    @Value("${banker.default.password:banker123}")
    private String bankerPassword;

    @Value("${banker.default.email:banker@wallet.com}")
    private String bankerEmail;

    @Override
    public void run(String... args) {
        if (!bankerRepository.existsByUsername(bankerUsername)) {
            Banker banker = Banker.builder()
                    .firstName("Super")
                    .lastName("Banker")
                    .email(bankerEmail)
                    .username(bankerUsername)
                    .passwordHash(passwordEncoder.encode(bankerPassword))
                    .role("SUPER_ADMIN") // 👑 Premier banker = super admin
                    .build();

            bankerRepository.save(banker);
            System.out.println("✅ Default SUPER_ADMIN banker created - Username: " + bankerUsername + " | Password: " + bankerPassword);
        } else {
            System.out.println("ℹ️ Banker account already exists");
        }
    }
}