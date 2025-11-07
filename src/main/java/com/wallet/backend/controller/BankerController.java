package com.wallet.backend.controller;

import com.wallet.backend.entities.Banker;
import com.wallet.backend.service.BankerService;
import com.wallet.backend.shared.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bankers")
@CrossOrigin(origins = "*")
public class BankerController {
    private final BankerService bankerService;

    public BankerController(BankerService bankerService) {
        this.bankerService = bankerService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','BANKER')")
    public List<Banker> getAllBankers() {
        return bankerService.getAllBankers();
    }

    @GetMapping("/{id}")
    public Banker getBankerById(@PathVariable Long id) {
        return bankerService.getBankerById(id);
    }

    @PostMapping
    public Banker createBanker(@RequestBody Banker banker) {
        return bankerService.createBanker(banker);
    }

    @PutMapping("/{id}")
    public Banker updateBanker(@PathVariable Long id, @RequestBody Banker banker) {
        return bankerService.updateBanker(id, banker);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<GlobalResponse<Banker>> updatePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String newPassword = request.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Le mot de passe ne peut pas être vide", null));
        }

        Banker updatedBanker = bankerService.updatePassword(id, newPassword);
        GlobalResponse<Banker> response = new GlobalResponse<>(
                true,
                "Mot de passe modifié avec succès",
                updatedBanker
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void deleteBanker(@PathVariable Long id) {
        bankerService.deleteBanker(id);
    }
}
