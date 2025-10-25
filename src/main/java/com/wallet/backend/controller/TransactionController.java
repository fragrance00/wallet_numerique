package com.wallet.backend.controllers;

import com.wallet.backend.entities.Transaction;
import com.wallet.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // Créer un transfert
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

    // Récupérer toutes les transactions
    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    // Rechercher les transactions par RIB
    @GetMapping("/search")
    public List<Transaction> getTransactionsByRib(@RequestParam String rib) {
        return transactionService.getTransactionsByRib(rib);
    }
}
