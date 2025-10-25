package com.wallet.backend.repository;

import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Récupère un compte par son numéro unique
    Optional<Account> findByAccountNumber(Long accountNumber);

    // Vérifie l'existence d'un compte par son numéro
    boolean existsByAccountNumber(Long accountNumber);

    // Récupère tous les comptes liés à un client
    List<Account> findByClient(Client client);
}
