package com.wallet.backend.controller;

import com.wallet.backend.entities.Credit;
import com.wallet.backend.entities.Status;
import com.wallet.backend.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
@CrossOrigin(origins = "*")
public class CreditController {

    @Autowired
    private CreditService creditService;

    // 🟢 Créer une demande de crédit
    @PostMapping
    public ResponseEntity<Credit> createCredit(@RequestBody Credit credit) {
        return ResponseEntity.ok(creditService.createCreditRequest(credit));
    }

    // 🔵 Récupérer toutes les demandes
    @GetMapping
    public List<Credit> getAllCredits() {
        return creditService.getAllCredits();
    }

    // 🟠 Modifier le statut d'une demande
    @PutMapping("/{id}/status")
    public ResponseEntity<Credit> updateCreditStatus(
            @PathVariable Long id,
            @RequestParam Status status) {
        return ResponseEntity.ok(creditService.updateCreditStatus(id, status));
    }

    // 🔴 Supprimer une demande
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredit(@PathVariable Long id) {
        creditService.deleteCredit(id);
        return ResponseEntity.noContent().build();
    }
}
