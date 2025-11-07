package com.wallet.backend.controller;

import com.wallet.backend.entities.Account;
import com.wallet.backend.entities.Client;
import com.wallet.backend.service.AccountService;
import com.wallet.backend.shared.GlobalResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    public ResponseEntity<GlobalResponse<Account>> createAccount(
            @RequestParam Long clientId,
            @RequestParam Long bankerId,
            @RequestParam String accountType,
            @RequestParam double initialDeposit
    ) {
        Account newAccount = accountService.createAccount(clientId, bankerId, accountType, initialDeposit);

        GlobalResponse<Account> response = new GlobalResponse<>(
                true,
                "Compte créé avec succès",
                newAccount
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{idAccount}/client")
    public ResponseEntity<GlobalResponse<Client>> getClientByAccountId(@PathVariable Long idAccount) {
        Client client = accountService.getClientByAccountId(idAccount);

        if (client != null) {
            return ResponseEntity.ok(
                    new GlobalResponse<>(true, "Client trouvé avec succès", client)
            );
        } else {
            return ResponseEntity.status(404).body(
                    new GlobalResponse<>(false, "Aucun client trouvé pour ce compte", null)
            );
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAccount (@PathVariable Long id){
         accountService.deleteAccount(id);
    }

    @GetMapping("/client/{id}")
    public List<Account> getAccountsByClient (@PathVariable Long id){
         return accountService.getAccountsByClientId(id);
    }
}
