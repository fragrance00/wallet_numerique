package com.wallet.backend.interfaces;

import com.wallet.backend.dto.TransferRequestDTO;
import com.wallet.backend.entities.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction createTransfer(Long fromAccountId, TransferRequestDTO transferRequest);
    List<Transaction> getTransactionsByAccount(Long accountId);
    List<Transaction> getAllTransactions();
    List<Transaction> getTransactionsByRib(Long rib);
}