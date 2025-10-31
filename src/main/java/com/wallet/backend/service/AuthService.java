package com.wallet.backend.service;

import com.wallet.backend.dto.AuthResponse;
import com.wallet.backend.dto.LoginRequest;
import com.wallet.backend.dto.SignupRequest;
import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.Client;
import com.wallet.backend.repository.BankerRepository;
import com.wallet.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class AuthService {

    @Autowired
    private BankerRepository bankerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public AuthResponse authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Object user = null;
        String userRole = "";
        Long userId = null;
        String userEmail = "";
        String userFirstName = "";
        String userLastName = "";

        // 🔍 CHERCHE DANS BANKER D'ABORD
        Banker banker = bankerRepository.findByUsername(username).orElse(null);
        if (banker != null && passwordEncoder.matches(password, banker.getPasswordHash())) {
            user = banker;
            userRole = banker.getRole(); // "SUPER_ADMIN" ou "BANKER"
            userId = banker.getId();
            userEmail = banker.getEmail();
            userFirstName = banker.getFirstName();
            userLastName = banker.getLastName();
        }
        // 🔍 SI PAS BANKER, CHERCHE DANS CLIENT
        else {
            Client client = clientRepository.findByEmail(username).orElse(null);
            if (client != null && passwordEncoder.matches(password, client.getPasswordHash())) {
                user = client;
                userRole = "CLIENT";
                userId = client.getId();
                userEmail = client.getEmail();
                userFirstName = client.getFirstName();
                userLastName = client.getLastName();
            }
        }

        // ❌ SI AUCUN UTILISATEUR TROUVÉ
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        // ✅ GÉNÈRE LE TOKEN
        String token = jwtService.generateToken(username, userRole, userId);

        return AuthResponse.builder()
                .token(token)
                .userType(userRole)
                .userId(userId)
                .firstName(userFirstName)
                .lastName(userLastName)
                .email(userEmail)
                .message("Login successful")
                .build();
    }

    public AuthResponse register(SignupRequest signupRequest) {
        String userType = signupRequest.getUserType();

        switch (userType.toUpperCase()) {
            case "BANKER":
                return registerBanker(signupRequest);
            case "CLIENT":
                return registerClient(signupRequest);
            default:
                throw new RuntimeException("Invalid user type for registration");
        }
    }

    private AuthResponse registerBanker(SignupRequest request) {
        if (bankerRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (bankerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Banker banker = Banker.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role("BANKER") // 👨‍💼 Nouveaux bankers = rôle normal
                .accounts(new ArrayList<>())
                .build();

        Banker savedBanker = bankerRepository.save(banker);

        String token = jwtService.generateToken(
                request.getUsername(),
                "BANKER",
                savedBanker.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .userType("BANKER")
                .userId(savedBanker.getId())
                .firstName(savedBanker.getFirstName())
                .lastName(savedBanker.getLastName())
                .email(savedBanker.getEmail())
                .message("Banker registered successfully")
                .build();
    }

    private AuthResponse registerClient(SignupRequest request) {
        if (clientRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Client client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .accounts(new ArrayList<>())
                .build();

        Client savedClient = clientRepository.save(client);

        String token = jwtService.generateToken(
                request.getEmail(),
                "CLIENT",
                savedClient.getId()
        );

        return AuthResponse.builder()
                .token(token)
                .userType("CLIENT")
                .userId(savedClient.getId())
                .firstName(savedClient.getFirstName())
                .lastName(savedClient.getLastName())
                .email(savedClient.getEmail())
                .message("Client registered successfully")
                .build();
    }
}