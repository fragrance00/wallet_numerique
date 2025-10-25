package com.wallet.backend.service;

import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Transaction;
import com.wallet.backend.repository.AccountRepository;
import com.wallet.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    // Créer un transfert
    public Transaction createTransaction(Transaction transaction) {
        Account fromAccount = accountRepository.findById(transaction.getFromAccount().getId())
                .orElseThrow(() -> new RuntimeException("Compte émetteur non trouvé"));
        Account toAccount = accountRepository.findById(transaction.getToAccount().getId())
                .orElseThrow(() -> new RuntimeException("Compte destinataire non trouvé"));

        double amount = transaction.getAmount();
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant !");
        }

        // Mise à jour des soldes
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Enregistrer la transaction
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    // Récupérer toutes les transactions
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    // Rechercher les transactions par RIB
    public List<Transaction> getTransactionsByRib(String rib) {
        return transactionRepository.findByFromAccount_AccountNumberOrToAccount_AccountNumber(rib, rib);
    }
}
