package com.wallet.backend.repository;

import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Trouver les transactions où le compte est émetteur ou destinataire
    List<Transaction> findByFromAccountOrToAccount(Account fromAccount, Account toAccount);

    // Trouver les transactions par RIB (émetteur ou destinataire)
    List<Transaction> findByFromAccount_AccountNumberOrToAccount_AccountNumber(Long fromAccountNumber, Long toAccountNumber);

    // Trouver les transactions d'un compte spécifique avec jointure
    @Query("SELECT t FROM Transaction t WHERE t.fromAccount.id = :accountId OR t.toAccount.id = :accountId ORDER BY t.timestamp DESC")
    List<Transaction> findTransactionsByAccountId(@Param("accountId") Long accountId);

    //trouver les transaction arive a un comtpe
    List<Transaction> findByToAccount_Id(Long accountId);
}