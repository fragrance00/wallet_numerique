package com.wallet.backend.controller;

import com.wallet.backend.dto.*;
import com.wallet.backend.entities.Banker;
import com.wallet.backend.interfaces.RealizedCreditService;
import com.wallet.backend.repository.BankerRepository;
import com.wallet.backend.shared.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/realized-credits")
@RequiredArgsConstructor
public class RealizedCreditController {

    private final RealizedCreditService realizedCreditService;
    private final BankerRepository bankerRepository;

    @PostMapping("/create/{accountId}")
    public ResponseEntity<GlobalResponse<RealizedCreditResponseDTO>> createRealizedCredit(
            @PathVariable Long accountId,
            @Valid @RequestBody RealizedCreditRequestDTO request) {

        RealizedCreditResponseDTO created = realizedCreditService.createCredit(request, accountId);
        return ResponseEntity.ok(new GlobalResponse<>(true, "Crédit réalisé créé avec succès", created));
    }

    @GetMapping
    public ResponseEntity<GlobalResponse<List<RealizedCreditResponseDTO>>> getAll() {
        return ResponseEntity.ok(new GlobalResponse<>(true, "Liste des crédits réalisés", realizedCreditService.getAllRealizedCredits()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<RealizedCreditResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new GlobalResponse<>(true, "Crédit réalisé trouvé", realizedCreditService.getRealizedCreditById(id)));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<GlobalResponse<List<RealizedCreditResponseDTO>>> getByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(new GlobalResponse<>(true, "Crédits réalisés pour le compte", realizedCreditService.getRealizedCreditsByAccount(accountId)));
    }

    @GetMapping("/banker/{bankerId}")
    public ResponseEntity<GlobalResponse<List<RealizedCreditResponseDTO>>> getByBanker(@PathVariable Long bankerId) {
        Banker banker = bankerRepository.findById(bankerId)
                .orElseThrow(() -> new IllegalArgumentException("Banquier introuvable"));
        return ResponseEntity.ok(new GlobalResponse<>(true, "Crédits réalisés pour le banquier", realizedCreditService.getRealizedCreditsByBanker(banker)));
    }
}
