package com.wallet.backend.controller;

import com.wallet.backend.dto.TransactionsByRibResponse;
import com.wallet.backend.dto.TransferRequestDTO;
import com.wallet.backend.dto.TransactionResponse;
import com.wallet.backend.entities.Transaction;
import com.wallet.backend.interfaces.TransactionService;
import com.wallet.backend.service.TransactionServiceImpl;
import com.wallet.backend.shared.Exceptions.GlobalSuccessHandler;
import com.wallet.backend.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionServiceImpl transactionServiceImp;

    @PostMapping("/transfer")
    public ResponseEntity<GlobalResponse<TransactionResponse>> createTransfer(
            @RequestParam Long fromAccountId,
            @RequestBody @Valid TransferRequestDTO transferRequest) {

        Transaction transaction = transactionService.createTransfer(fromAccountId, transferRequest);
        TransactionResponse response = mapToTransactionResponse(transaction);

        return GlobalSuccessHandler.created("Transfert effectué avec succès", response);
    }

    @GetMapping("/my-transactions")
    public ResponseEntity<GlobalResponse<List<TransactionResponse>>> getMyTransactions(
            @RequestParam Long accountId) {

        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountId);
        List<TransactionResponse> responses = transactions.stream()
                .map(this::mapToTransactionResponse)
                .toList();

        return GlobalSuccessHandler.success("Historique des transactions récupéré avec succès", responses);
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<TransactionResponse>>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionResponse> responses = transactions.stream()
                .map(this::mapToTransactionResponse)
                .toList();

        return GlobalSuccessHandler.success("Toutes les transactions récupérées avec succès", responses);
    }

    @GetMapping("/search-separated/{rib}")
    public ResponseEntity<GlobalResponse<TransactionsByRibResponse>> getTransactionsByRibSeparated(
            @PathVariable Long rib) {

        Map<String, List<Transaction>> transactions = transactionService.getTransactionsByRibSeparated(rib);

        // Convertir en DTOs
        List<TransactionResponse> envoyees = transactions.get("transactionsEnvoyees").stream()
                .map(this::mapToTransactionResponse)
                .toList();

        List<TransactionResponse> recues = transactions.get("transactionsRecues").stream()
                .map(this::mapToTransactionResponse)
                .toList();

        TransactionsByRibResponse response = new TransactionsByRibResponse(envoyees, recues);

        return GlobalSuccessHandler.success("Transactions séparées pour le RIB: " + rib, response);
    }
    @GetMapping("/account/{accountId}")
    public ResponseEntity<GlobalResponse<List<TransactionResponse>>> getTransactionsForAccount(
            @PathVariable Long accountId) {

        List<Transaction> transactions = transactionService.getTransactionsByAccount(accountId);
        List<TransactionResponse> responses = transactions.stream()
                .map(this::mapToTransactionResponse)
                .toList();

        return GlobalSuccessHandler.success("Transactions du compte récupérées avec succès", responses);
    }

    // Méthode de mapping CORRIGÉE
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setTimestamp(transaction.getTimestamp().toString());
        response.setLocation(transaction.getLocation()); // ✅ AJOUTEZ CETTE LIGNE
        response.setFromAccountId(transaction.getFromAccount().getId());
        response.setToAccountId(transaction.getToAccount().getId());
        response.setFromAccountNumber(transaction.getFromAccount().getAccountNumber().toString());
        response.setToAccountNumber(transaction.getToAccount().getAccountNumber().toString());

        return response;
    }

    @GetMapping("/sent/{accountId}")
    //session
    public ResponseEntity<List<Transaction>> getSentTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionServiceImp.getReceivedTransactions(accountId));
    }
}