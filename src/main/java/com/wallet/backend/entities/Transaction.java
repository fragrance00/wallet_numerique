package com.wallet.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private String type; // dépôt, retrait, transfert
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount; // compte émetteur

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount; // compte destinataire
}
