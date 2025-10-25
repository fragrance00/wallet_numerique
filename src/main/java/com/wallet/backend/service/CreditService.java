package com.wallet.backend.service;

import com.wallet.backend.entities.Credit;
import com.wallet.backend.entities.Status;
import com.wallet.backend.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditService {

    @Autowired
    private CreditRepository creditRepository;

    // 🔹 Créer une demande de crédit
    public Credit createCreditRequest(Credit credit) {
        credit.setRequestDate(LocalDateTime.now());
        credit.setStatus(Status.PENDING); // par défaut en attente
        return creditRepository.save(credit);
    }

    // 🔹 Récupérer toutes les demandes
    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    // 🔹 Modifier le statut du crédit
    public Credit updateCreditStatus(Long id, Status status) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crédit non trouvé"));
        credit.setStatus(status);
        return creditRepository.save(credit);
    }

    // 🔹 Supprimer une demande
    public void deleteCredit(Long id) {
        creditRepository.deleteById(id);
    }
}
