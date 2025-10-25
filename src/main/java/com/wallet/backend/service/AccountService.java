package com.wallet.backend.service;

import com.wallet.backend.entities.Account;
import com.wallet.backend.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        Account existing = getAccountById(id);
        existing.setBalance(updatedAccount.getBalance());
        existing.setAccountNumber(updatedAccount.getAccountNumber());
        existing.setPasswordHash(updatedAccount.getPasswordHash());
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
