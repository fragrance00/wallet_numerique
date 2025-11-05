package com.wallet.backend.controller;

import com.wallet.backend.dto.AccountDto;
import com.wallet.backend.entities.Account;
import com.wallet.backend.repository.AccountRepository;
import com.wallet.backend.service.AccountAuthService;
import com.wallet.backend.service.AccountConverterService;
import com.wallet.backend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account-auth")
@CrossOrigin(origins = "*")
public class AccountAuthController {

    private final AccountAuthService accountAuthService;
    private final JwtService jwtService;
    private final AccountConverterService accountConverterService;
    private final AccountRepository accountRepository;

    public AccountAuthController(AccountAuthService accountAuthService,
                                 JwtService jwtService,
                                 AccountConverterService accountConverterService,
                                 AccountRepository accountRepository) {
        this.accountAuthService = accountAuthService;
        this.jwtService = jwtService;
        this.accountConverterService = accountConverterService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAccount(
            @RequestBody AccountAuthRequest request,
            Authentication authentication) {

        try {
            String userEmail = authentication.getName();
            String userType = extractUserTypeFromAuthentication(authentication);

            Account account;
            String accountToken;

            // ✅ SOLUTION RAPIDE : Si c'est un SUPER_ADMIN, contourner les vérifications normales
            if ("SUPER_ADMIN".equals(userType)) {
                account = accountRepository.findById(request.getAccountId())
                        .orElseThrow(() -> new RuntimeException("Compte introuvable avec l'id: " + request.getAccountId()));

                // Générer un token spécial pour Super Admin
                Map<String, Object> extraClaims = new HashMap<>();
                extraClaims.put("userType", "SUPER_ADMIN_ACCOUNT");
                extraClaims.put("userId", 0L); // ID spécial pour Super Admin
                extraClaims.put("accountId", request.getAccountId());
                extraClaims.put("tokenType", "ACCOUNT_ACCESS");

                accountToken = jwtService.generateToken(userEmail, "SUPER_ADMIN_ACCOUNT", 0L, extraClaims);

            } else {
                // Logique normale pour les CLIENTS
                account = accountAuthService.authenticateAccountAccess(
                        request.getAccountId(),
                        userEmail,
                        request.getAccountPassword()
                );

                accountToken = accountAuthService.generateAccountToken(
                        userEmail,
                        request.getAccountId(),
                        account.getClient().getId()
                );
            }

            AccountDto accountDto = accountConverterService.convertToDto(account);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Authentification réussie");
            response.put("accountToken", accountToken);
            response.put("account", accountDto);
            response.put("userType", userType); // Pour debug

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/accounts")
    public ResponseEntity<?> getClientAccounts(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            String userType = extractUserTypeFromAuthentication(authentication);

            List<Account> accounts;

            // ✅ Si Super Admin, retourner TOUS les comptes
            if ("SUPER_ADMIN".equals(userType)) {
                accounts = accountRepository.findAll();
            } else {
                // Sinon, retourner seulement les comptes du client
                accounts = accountAuthService.getClientAccounts(userEmail);
            }

            List<AccountDto> accountDtos = new ArrayList<>();
            for (Account account : accounts) {
                AccountDto dto = accountConverterService.convertToDto(account);
                accountDtos.add(dto);
            }

            return ResponseEntity.ok(accountDtos);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    @PostMapping("/validate-account-token")
    public ResponseEntity<Boolean> validateAccountToken(
            @RequestHeader("Authorization") String token,
            @RequestParam Long accountId) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            String email = jwtService.extractUsername(token);
            boolean isValid = jwtService.validateAccountToken(token, email, accountId);

            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    // ✅ NOUVELLE MÉTHODE : Extraire le userType de l'authentication
    private String extractUserTypeFromAuthentication(Authentication authentication) {
        try {
            // Si l'authentication contient des credentials JWT
            if (authentication.getCredentials() instanceof String) {
                String token = (String) authentication.getCredentials();
                return jwtService.extractUserType(token);
            }

            // Sinon, essayer d'extraire du principal
            if (authentication.getPrincipal() instanceof String) {
                String principal = (String) authentication.getPrincipal();
                // Logique pour extraire le userType du principal si nécessaire
            }

            // Par défaut, considérer comme CLIENT
            return "CLIENT";
        } catch (Exception e) {
            return "CLIENT";
        }
    }

    // ✅ ENDPOINT DE DEBUG : Vérifier l'authentication
    @GetMapping("/debug/auth")
    public ResponseEntity<?> debugAuthentication(Authentication authentication) {
        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("name", authentication.getName());
        debugInfo.put("authorities", authentication.getAuthorities());
        debugInfo.put("credentials", authentication.getCredentials() != null ? "Present" : "Null");
        debugInfo.put("principal", authentication.getPrincipal());
        debugInfo.put("userType", extractUserTypeFromAuthentication(authentication));

        return ResponseEntity.ok(debugInfo);
    }

    public static class AccountAuthRequest {
        private Long accountId;
        private String accountPassword;

        public Long getAccountId() { return accountId; }
        public void setAccountId(Long accountId) { this.accountId = accountId; }
        public String getAccountPassword() { return accountPassword; }
        public void setAccountPassword(String accountPassword) { this.accountPassword = accountPassword; }
    }
}