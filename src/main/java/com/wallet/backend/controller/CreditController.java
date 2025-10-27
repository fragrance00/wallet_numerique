package com.wallet.backend.controller;

import com.wallet.backend.entities.Client;
import com.wallet.backend.entities.Credit;
import com.wallet.backend.entities.Status;
import com.wallet.backend.service.CreditService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/credits")
@CrossOrigin(origins = "*")
public class CreditController {

    @Autowired
    private CreditService creditService;

    // 🟢 Créer une demande de crédit
    @PostMapping
    public ResponseEntity<Credit> createCredit(
            @RequestBody Credit credit,
            @RequestPart(value = "file", required = false) MultipartFile file/*, HttpSession session*/
    ) throws IOException {

        // Dossier d’upload local (peut être changé plus tard pour S3)
        /*String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // Sauvegarde du fichier si présent
        if (file != null && !file.isEmpty()) {
            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            credit.setDocuments(filePath);
        }*/

        // Date et statut IdAccount définis automatiquement
        credit.setRequestDate(java.time.LocalDateTime.now());
        credit.setStatus(Status.PENDING);
        /*Long accountId = (Long) session.getAttribute("accountId");*/

        Credit saved = creditService.createCreditRequest(credit/*,accountId*/);
        return ResponseEntity.ok(saved);
    }


    // 🔵 Récupérer toutes les demandes
    @GetMapping
    public ResponseEntity<List<Credit>> getAllCredits() {
        return ResponseEntity.ok(creditService.getAllCredits());
    }

    //Lister tous les crédits en cours (PENDING)
    @GetMapping("/pending")
    public ResponseEntity<List<Credit>> getPendingCredits() {
        return ResponseEntity.ok(creditService.getCreditsByStatus(Status.PENDING));
    }

    // 🟠 Modifier le statut d'une demande
    //exemple de requete HTTP "http://localhost:8080/api/credits/1/status?status=APPROVED"
    @PutMapping("/{id}/status")
    public ResponseEntity<Credit> updateCreditStatus(
            @PathVariable Long id,
            @RequestParam Status status) {
        return ResponseEntity.ok(creditService.updateCreditStatus(id, status));
    }

    // getCredits By Account
    //http://localhost:8080/api/credits/account/6
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Credit>> getCreditsByAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(creditService.getCreditsByAccount(accountId));
    }

    @GetMapping("/{creditId}/client")
    public ResponseEntity<Client> getClientByCredit(@PathVariable Long creditId) {
        return ResponseEntity.ok(creditService.getClientByCredit(creditId));
    }

    //Supprimer une demande
    //http://localhost:8080/api/credits/1/client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredit(@PathVariable Long id) {
        creditService.deleteCredit(id);
        return ResponseEntity.noContent().build();
    }

    // affichage de PDF
    /*@GetMapping("/view/{id}")
public ResponseEntity<Void> viewCreditDocument(@PathVariable Long id) {
    Credit credit = creditService.getCreditById(id);

    // Vérifie que le document existe
    if (credit.getDocuments() == null || credit.getDocuments().isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    // Redirection vers le lien du document stocké (S3 ou autre cloud)
    return ResponseEntity
            .status(302) // Code HTTP de redirection
            .header("Location", credit.getDocuments())
            .build();
}
*/
    /*@GetMapping("/view/{id}")
public ResponseEntity<Void> viewCreditDocument(@PathVariable Long id) {
    Credit credit = creditService.getCreditById(id);

    // Vérifie que le document existe
    if (credit.getDocuments() == null || credit.getDocuments().isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    // Redirection vers le lien du document stocké (S3 ou autre cloud)
    return ResponseEntity
            .status(302) // Code HTTP de redirection
            .header("Location", credit.getDocuments())
            .build();
}
*/
}
