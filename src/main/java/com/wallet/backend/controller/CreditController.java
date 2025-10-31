package com.wallet.backend.controller;

import com.wallet.backend.dto.CreditRequestDTO;
import com.wallet.backend.dto.CreditResponseDTO;
import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.Status;
import com.wallet.backend.interfaces.CreditService;
import com.wallet.backend.repository.BankerRepository;
import com.wallet.backend.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
public class CreditController {

    private final CreditService creditService;

    @Autowired
    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @Autowired
    private BankerRepository bankerRepository;

    // 🔹 Créer un crédit
    @PostMapping("/create/{accountId}")
    public ResponseEntity<GlobalResponse<CreditResponseDTO>> createCredit(
            @PathVariable Long accountId,
            @Valid @RequestBody CreditRequestDTO request) {

        CreditResponseDTO created = creditService.createCredit(request, accountId);
        GlobalResponse<CreditResponseDTO> response =
                new GlobalResponse<>(true, "Crédit créé avec succès", created);

        return ResponseEntity.ok(response);
    }

    // 🔹 Récupérer tous les crédits
    @GetMapping
    public ResponseEntity<GlobalResponse<List<CreditResponseDTO>>> getAllCredits() {
        List<CreditResponseDTO> credits = creditService.getAllCredits();
        GlobalResponse<List<CreditResponseDTO>> response =
                new GlobalResponse<>(true, "Liste de tous les crédits récupérée", credits);

        return ResponseEntity.ok(response);
    }

    // 🔹 Récupérer un crédit par ID
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<CreditResponseDTO>> getCreditById(@PathVariable Long id) {
        CreditResponseDTO credit = creditService.getCreditById(id);
        GlobalResponse<CreditResponseDTO> response =
                new GlobalResponse<>(true, "Crédit trouvé", credit);

        return ResponseEntity.ok(response);
    }

    // 🔹 Mettre à jour le statut d’un crédit
    @PutMapping("/{id}/status")
    public ResponseEntity<GlobalResponse<CreditResponseDTO>> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status) {

        CreditResponseDTO updated = creditService.updateCreditStatus(id, status);
        GlobalResponse<CreditResponseDTO> response =
                new GlobalResponse<>(true, "Statut du crédit mis à jour avec succès", updated);

        return ResponseEntity.ok(response);
    }

    // 🔹 Récupérer les crédits selon le statut
    @GetMapping("/status/{status}")
    public ResponseEntity<GlobalResponse<List<CreditResponseDTO>>> getCreditsByStatus(
            @PathVariable Status status) {

        List<CreditResponseDTO> credits = creditService.getCreditsByStatus(status);
        GlobalResponse<List<CreditResponseDTO>> response =
                new GlobalResponse<>(true, "Crédits avec le statut " + status + " récupérés", credits);

        return ResponseEntity.ok(response);
    }

    // 🔹 Récupérer les crédits d’un compte
    @GetMapping("/account/{accountId}")
    public ResponseEntity<GlobalResponse<List<CreditResponseDTO>>> getCreditsByAccount(
            @PathVariable Long accountId) {

        List<CreditResponseDTO> credits = creditService.getCreditsByAccount(accountId);
        GlobalResponse<List<CreditResponseDTO>> response =
                new GlobalResponse<>(true, "Crédits du compte récupérés", credits);

        return ResponseEntity.ok(response);
    }

    // 🔹 Supprimer un crédit
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteCredit(@PathVariable Long id) {
        creditService.deleteCredit(id);
        GlobalResponse<Void> response =
                new GlobalResponse<>(true, "Crédit supprimé avec succès");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/banker/{bankerId}")
    public ResponseEntity<GlobalResponse<List<CreditResponseDTO>>> getCreditsByBanker(
            @PathVariable Long bankerId) {

        // Récupérer le banker (tu peux utiliser un BankerRepository)
        Banker banker = bankerRepository.findById(bankerId)
                .orElseThrow(() -> new IllegalArgumentException("Banquier introuvable avec ID: " + bankerId));

        List<CreditResponseDTO> credits = creditService.getCreditsByBanker(banker);
        GlobalResponse<List<CreditResponseDTO>> response =
                new GlobalResponse<>(true, "Crédits du banquier récupérés", credits);

        return ResponseEntity.ok(response);
    }
}
