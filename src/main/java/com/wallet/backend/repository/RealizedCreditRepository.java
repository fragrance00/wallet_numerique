package com.wallet.backend.repository;

import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.RealizedCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealizedCreditRepository extends JpaRepository<RealizedCredit, Long> {

    // 1. Crédits réalisés selon le compte
    List<RealizedCredit> findByCredit_Account_Id(Long accountId);
    // 2. Crédits réalisés selon le banker
    List<RealizedCredit> findByCredit_Account_Banker(Banker banker);
    /*@Query("SELECT rc FROM RealizedCredit rc WHERE rc.credit.account IN " +
            "(SELECT a FROM Account a WHERE a.banker = :banker)")
    List<RealizedCredit> findByBanker(@Param("banker") Banker banker);*/
}
