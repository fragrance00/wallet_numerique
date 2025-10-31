package com.wallet.backend.repository;

import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.Credit;
import com.wallet.backend.entities.RealizedCredit;
import com.wallet.backend.entities.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findByStatus(Status status);
    List<Credit> findByAccountId(Long accountId);

    //selection des credit selon le banker
    @Query("SELECT c FROM Credit c WHERE c.account IN " +
            "(SELECT a FROM Account a WHERE a.banker = :banker)")
    List<Credit> findByBanker(@Param("banker") Banker banker);
}
