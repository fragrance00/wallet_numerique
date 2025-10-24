package com.wallet.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;           // montant demandé
    private String purpose;          // cause ou objet du crédit
    private String documents;        // chemin ou lien vers documents soumis par le client
    private LocalDateTime requestDate; // date de la demande

    @Enumerated(EnumType.STRING)
    private Status status;           // état de la demande

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;         // compte lié à la demande de crédit
}
