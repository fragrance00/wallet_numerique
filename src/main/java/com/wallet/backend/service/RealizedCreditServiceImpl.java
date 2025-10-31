package com.wallet.backend.service;

import com.wallet.backend.entities.*;
import com.wallet.backend.repository.AccountRepository;
import com.wallet.backend.repository.CreditRepository;
import com.wallet.backend.repository.RealizedCreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RealizedCreditService {

    @Autowired
    private RealizedCreditRepository realizedCreditRepository;

    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private AccountRepository accountRepository;

    public RealizedCredit createRealizedCredit(Long creditId, double interestRate, int months) {
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Crédit introuvable"));

        // Changer le statut du crédit
        //credit.setStatus(Status.REALIZED);
        //creditRepository.save(credit);

        double amount = credit.getAmount();
        double totalToPay = amount * (1 + interestRate / 100);
        double monthlyPayment = totalToPay / months;

        // Créditer le compte du client
        Account account = credit.getAccount();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        RealizedCredit realized = RealizedCredit.builder()
                .credit(credit)
                .interestRate(interestRate)
                .monthlyPayment(monthlyPayment)
                .totalToPay(totalToPay)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(months))
                .build();

        return realizedCreditRepository.save(realized);
    }

    public RealizedCreditService(RealizedCreditRepository realizedCreditRepository) {
        this.realizedCreditRepository = realizedCreditRepository;
    }

    // Crédits réalisés selon le compte
    public List<RealizedCredit> getCreditsByAccount(Account account) {
        return realizedCreditRepository.findByCredit_Account(account);
    }

    // Crédits réalisés selon le banker
    public List<RealizedCredit> getCreditsByBanker(Banker banker) {
        return realizedCreditRepository.findByBanker(banker);
    }

}
