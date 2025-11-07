package com.wallet.backend.service;

import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.Client;
import com.wallet.backend.repository.AccountRepository;
import com.wallet.backend.repository.BankerRepository;
import com.wallet.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;

    private final BankerRepository bankerRepository;

    public AccountService(AccountRepository accountRepository,BankerRepository bankerRepository,ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.bankerRepository = bankerRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte introuvable avec l'id: " + id));
    }

    public List<Account> getAccountsByClientId(Long clientId) {
        return accountRepository.findByClientId(clientId);
    }

    public Client getClientByAccountId(Long idAccount) {
        Optional<Account> accountOpt = accountRepository.findById(idAccount);
        return accountOpt.map(Account::getClient).orElse(null);
    }

    private Long generateUniqueRib() {
        Random random = new Random();
        Long rib;
        do {
            rib = 1_000_000_000_0000L + (long) (random.nextDouble() * 9_000_000_000_0000L);
        } while (accountRepository.existsByAccountNumber(rib));
        return rib;
    }

    public Account createAccount(Long clientId, Long bankerId, String accountType, double initialDeposit) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        Banker banker = bankerRepository.findById(bankerId)
                .orElseThrow(() -> new RuntimeException("Banquier non trouvé"));

        // Génération d’un numéro de compte aléatoire (tu peux le rendre plus sophistiqué)
        Long accountNumber = 1000000000L + new Random().nextLong(900000000L);

        Account account = Account.builder()
                .accountNumber(accountNumber)
                .balance(initialDeposit)
                .client(client)
                .banker(banker)
                .password("account123") // mot de passe par défaut si besoin
                .build();

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