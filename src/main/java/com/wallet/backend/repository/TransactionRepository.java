package com.wallet.backend.repository;

import com.wallet.backend.entities.Transaction;
import com.wallet.backend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Rechercher toutes les transactions liées à un RIB (émetteur ou destinataire)
    List<Transaction> findByFromAccount_AccountNumberOrToAccount_AccountNumber(String fromRib, String toRib);
}
