package com.wallet.backend.controller;

import com.wallet.backend.dto.TransferRequestDTO;
import com.wallet.backend.dto.TransactionResponse;
import com.wallet.backend.entities.Transaction;
import com.wallet.backend.interfaces.TransactionService;
import com.wallet.backend.shared.Exceptions.GlobalSuccessHandler;
import com.wallet.backend.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

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

    @GetMapping("/search")
    public ResponseEntity<GlobalResponse<List<TransactionResponse>>> getTransactionsByRib(@RequestParam Long rib) {
        List<Transaction> transactions = transactionService.getTransactionsByRib(rib);
        List<TransactionResponse> responses = transactions.stream()
                .map(this::mapToTransactionResponse)
                .toList();

        return GlobalSuccessHandler.success("Transactions trouvées pour le RIB: " + rib, responses);
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

    // Méthode de mapping
    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setTimestamp(transaction.getTimestamp().toString());
        response.setFromAccountId(transaction.getFromAccount().getId());
        response.setToAccountId(transaction.getToAccount().getId());
        response.setFromAccountNumber(transaction.getFromAccount().getAccountNumber().toString());
        response.setToAccountNumber(transaction.getToAccount().getAccountNumber().toString());

        return response;
    }
}