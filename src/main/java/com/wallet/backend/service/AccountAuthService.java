package com.wallet.backend.service;

import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Client;
import com.wallet.backend.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AccountAuthService {

    private final AccountRepository accountRepository;
    private final ClientService clientService;
    private final JwtService jwtService;

    public AccountAuthService(AccountRepository accountRepository,
                              ClientService clientService,
                              JwtService jwtService) {
        this.accountRepository = accountRepository;
        this.clientService = clientService;
        this.jwtService = jwtService;
    }

    /**
     * Vérifie si le compte appartient au client authentifié
     */
    public boolean isAccountOwnedByClient(Long accountId, String clientEmail) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        Client client = clientService.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        return account.getClient() != null &&
                account.getClient().getId().equals(client.getId());
    }

    /**
     * Authentifie l'accès à un compte spécifique avec le mot de passe du compte
     */
    public Account authenticateAccountAccess(Long accountId, String clientEmail, String accountPassword) {
        // Vérifier que le compte appartient au client
        if (!isAccountOwnedByClient(accountId, clientEmail)) {
            throw new RuntimeException("Ce compte ne vous appartient pas");
        }

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        // Vérifier le mot de passe du compte (comparaison directe sans hash)
        if (!account.getPassword().equals(accountPassword)) {
            throw new RuntimeException("Mot de passe du compte incorrect");
        }

        return account;
    }

    /**
     * Récupère tous les comptes d'un client
     */
    public java.util.List<Account> getClientAccounts(String clientEmail) {
        Client client = clientService.findByEmail(clientEmail)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        return accountRepository.findByClientId(client.getId());
    }

    /**
     * Génère un token spécifique pour l'accès au compte
     */
    public String generateAccountToken(String clientEmail, Long accountId, Long clientId) {
        // Ajouter des claims spécifiques au compte
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userType", "CLIENT_ACCOUNT");
        extraClaims.put("userId", clientId);
        extraClaims.put("accountId", accountId);
        extraClaims.put("tokenType", "ACCOUNT_ACCESS");

        return jwtService.generateToken(clientEmail, "CLIENT_ACCOUNT", clientId, extraClaims);
    }
}