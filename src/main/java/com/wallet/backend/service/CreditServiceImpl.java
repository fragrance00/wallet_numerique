package com.wallet.backend.service;

import com.wallet.backend.entities.*;
import com.wallet.backend.repository.AccountRepository;
import com.wallet.backend.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    /*@Autowired
    private final AccountRepository accountRepository;*/



    // 🔹 Créer une demande de crédit
    public Credit createCreditRequest(Credit credit/*,Long accountId*/) {
        /*Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        credit.setAccount(account);*/
        return creditRepository.save(credit);
    }

    // 🔹 Récupérer toutes les demandes
    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    //Lister tous les crédits avec statut spécifique
    public List<Credit> getCreditsByStatus(Status status) {
        return creditRepository.findByStatus(status);
    }


    // 🔹 Modifier le statut du crédit
    public Credit updateCreditStatus(Long id, Status status) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crédit non trouvé"));
        credit.setStatus(status);
        return creditRepository.save(credit);
    }

    //Lister les crédits d’un compte spécifique
    public List<Credit> getCreditsByAccount(Long accountId) {
        return creditRepository.findByAccountId(accountId);
    }



    // Crédits réalisés selon le banker
    public List<Credit> getCreditsByBanker(Banker banker) {
        return creditRepository.findByBanker(banker);
    }

    //Obtenir les infos du client ayant fait la demande
    public Client getClientByCredit(Long creditId) {
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Crédit introuvable"));
        return credit.getAccount().getClient();
    }
    // 🔹 Supprimer une demande
    public void deleteCredit(Long id) {
        creditRepository.deleteById(id);
    }
}
