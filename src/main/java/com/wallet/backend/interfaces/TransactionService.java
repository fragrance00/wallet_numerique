package com.wallet.backend.interfaces;

import com.wallet.backend.dto.TransferRequestDTO;
import com.wallet.backend.entities.Transaction;
import java.util.List;
import java.util.Map;

public interface TransactionService {
    Transaction createTransfer(Long fromAccountId, TransferRequestDTO transferRequest);
    List<Transaction> getTransactionsByAccount(Long accountId);
    List<Transaction> getAllTransactions();

    Map<String, List<Transaction>> getTransactionsByRibSeparated(Long rib);
}