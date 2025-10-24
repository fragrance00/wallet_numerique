package com.wallet.backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String accountNumber; // RIB ou identifiant bancaire

    private double balance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client; // le propriétaire du compte

    @ManyToOne
    @JoinColumn(name = "banker_id")
    private Banker banker; // le banquier qui gère/crée le compte

    // Mot de passe spécifique au compte (optionnel pour verrouillage du compte)
    private String passwordHash;

    // Transactions sortantes depuis ce compte
    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> outgoingTransactions;

    // Transactions entrantes vers ce compte
    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> incomingTransactions;

    // Crédits liés à ce compte
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Credit> credits;
}
