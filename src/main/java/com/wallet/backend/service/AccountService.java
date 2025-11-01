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

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable avec l'id: " + id));
    }

    private Long generateUniqueRib() {
        Random random = new Random();
        Long rib;
        do {
            rib = 1_000_000_000_0000L + (long) (random.nextDouble() * 9_000_000_000_0000L);
        } while (accountRepository.existsByAccountNumber(rib));
        return rib;
    }

    public Account createAccount(Account account) {
        if (account.getAccountNumber() == null) {
            account.setAccountNumber(generateUniqueRib());
        }
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        Account existing = getAccountById(id);
        existing.setBalance(updatedAccount.getBalance());
        existing.setAccountNumber(updatedAccount.getAccountNumber());
        existing.setPassword(updatedAccount.getPassword()); // ⚠️ CHANGEMENT : passwordHash → password
        existing.setClient(updatedAccount.getClient());
        existing.setBanker(updatedAccount.getBanker());
        return accountRepository.save(existing);
    }

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Compte introuvable avec l'id: " + id);
        }
        accountRepository.deleteById(id);
    }
}