package com.wallet.backend.service;

import com.wallet.backend.dto.TransferRequestDTO;
import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Transaction;
import com.wallet.backend.interfaces.TransactionService;
import com.wallet.backend.repository.AccountRepository;
import com.wallet.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Transactional
    public Transaction createTransfer(Long fromAccountId, TransferRequestDTO transferRequest) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Compte émetteur non trouvé"));

        Account toAccount = accountRepository.findByAccountNumber(transferRequest.getRecipientRIB())
                .orElseThrow(() -> new RuntimeException("Compte destinataire non trouvé"));

        double amount = transferRequest.getAmount();

        // Vérifications
        if (fromAccount.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant ! Solde actuel: " + fromAccount.getBalance());
        }

        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new RuntimeException("Impossible de transférer vers votre propre compte");
        }

        // Mise à jour des soldes
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // Créer la transaction
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type("TRANSFERT")
                .timestamp(LocalDateTime.now())
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .build();

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findTransactionsByAccountId(accountId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public List<Transaction> getTransactionsByRib(Long rib) {
        return transactionRepository.findByFromAccount_AccountNumberOrToAccount_AccountNumber(rib, rib);
    }
}