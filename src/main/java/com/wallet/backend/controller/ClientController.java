package com.wallet.backend.controller;

import com.wallet.backend.entities.Client;
import com.wallet.backend.service.ClientService;
import com.wallet.backend.shared.GlobalResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*") // autoriser le frontend
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Client>> createClient(@RequestBody Client client) {
        Client saved = clientService.createClient(client);

        GlobalResponse<Client> response = new GlobalResponse<>(
                true,
                "Client créé avec succès",
                saved
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        return ResponseEntity.ok(clientService.updateClient(id, client));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<GlobalResponse<Client>> updatePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        String newPassword = request.get("newPassword");
        if (newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new GlobalResponse<>(false, "Le mot de passe ne peut pas être vide", null));
        }

        Client updatedClient = clientService.updatePassword(id, newPassword);
        GlobalResponse<Client> response = new GlobalResponse<>(
                true,
                "Mot de passe modifié avec succès",
                updatedClient
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
