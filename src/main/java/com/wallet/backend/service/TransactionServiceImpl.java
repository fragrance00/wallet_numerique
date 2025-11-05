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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private GeoLocationService geoLocationService;

    @Override
    @Transactional
    public Transaction createTransfer(Long fromAccountId, TransferRequestDTO transferRequest) {
        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Compte émetteur non trouvé"));

        // CORRECTION : Utiliser findByAccountNumber au lieu de existsByAccountNumber
        Account toAccount = accountRepository.findByAccountNumber(transferRequest.getRecipientRIB())
                .orElseThrow(() -> new RuntimeException("Compte destinataire non trouvé avec le RIB: " + transferRequest.getRecipientRIB()));

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

        // Récupération de la localisation
        String location = geoLocationService.getClientLocation();
        System.out.println("📍 Localisation détectée: " + location);

        // Créer la transaction AVEC la localisation
        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type("TRANSFERT")
                .timestamp(LocalDateTime.now())
                .location(location)
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        System.out.println("💾 Transaction sauvegardée avec location: " + savedTransaction.getLocation());

        return savedTransaction;
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
    public Map<String, List<Transaction>> getTransactionsByRibSeparated(Long rib) {
        List<Transaction> allTransactions = transactionRepository.findByFromAccount_AccountNumberOrToAccount_AccountNumber(rib, rib);

        // Séparation en deux groupes
        List<Transaction> transactionsEnvoyees = allTransactions.stream()
                .filter(t -> t.getFromAccount().getAccountNumber().equals(rib))
                .collect(Collectors.toList());

        List<Transaction> transactionsRecues = allTransactions.stream()
                .filter(t -> t.getToAccount().getAccountNumber().equals(rib))
                .collect(Collectors.toList());

        Map<String, List<Transaction>> result = new HashMap<>();
        result.put("transactionsEnvoyees", transactionsEnvoyees);
        result.put("transactionsRecues", transactionsRecues);

        return result;
    }

    // ✅ NOUVELLE MÉTHODE POUR TransactionController
    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction non trouvée avec l'id: " + id));
    }
}