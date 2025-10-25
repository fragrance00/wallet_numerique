package com.wallet.backend.service;

import com.wallet.backend.entities.Account;
import com.wallet.backend.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Récupérer tous les comptes
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Récupérer un compte par son ID
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable avec l'id: " + id));
    }

    // --- Génération d'un RIB unique de 14 chiffres ---
    private Long generateUniqueRib() {
        Random random = new Random();
        Long rib;
        do {
            // 14 chiffres : 1_000_000_000_0000L à 9_999_999_999_9999L
            rib = 1_000_000_000_0000L + (long) (random.nextDouble() * 9_000_000_000_0000L);
        } while (accountRepository.existsByAccountNumber(rib));
        return rib;
    }

    // Création de compte avec génération automatique du RIB si non fourni
    public Account createAccount(Account account) {
        if (account.getAccountNumber() == null) {
            account.setAccountNumber(generateUniqueRib());
        }
        return accountRepository.save(account);
    }

    // Mise à jour d'un compte existant
    public Account updateAccount(Long id, Account updatedAccount) {
        Account existing = getAccountById(id);
        existing.setBalance(updatedAccount.getBalance());
        existing.setAccountNumber(updatedAccount.getAccountNumber());
        existing.setPasswordHash(updatedAccount.getPasswordHash());
        existing.setClient(updatedAccount.getClient());
        existing.setBanker(updatedAccount.getBanker());
        return accountRepository.save(existing);
    }

    // Suppression d'un compte par ID
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Compte introuvable avec l'id: " + id);
        }
        accountRepository.deleteById(id);
    }
}
