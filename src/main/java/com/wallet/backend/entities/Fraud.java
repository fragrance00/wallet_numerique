package com.wallet.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "frauds")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fraud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;           // description de la fraude
    private LocalDateTime detectedAt;     // date et heure de détection
    private double amount;                // montant suspect

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;      // transaction associée
}
