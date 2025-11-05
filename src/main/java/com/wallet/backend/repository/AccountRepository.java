package com.wallet.backend.repository;

import com.wallet.backend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(Long accountNumber);
    List<Account> findByClientId(Long clientId);

    // AJOUTEZ CETTE MÉTHODE MANQUANTE
    Optional<Account> findByAccountNumber(Long accountNumber);
}