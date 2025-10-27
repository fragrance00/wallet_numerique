package com.wallet.backend.repository;

import com.wallet.backend.entities.Credit;
import com.wallet.backend.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findByStatus(Status status);
    List<Credit> findByAccountId(Long accountId);
}
