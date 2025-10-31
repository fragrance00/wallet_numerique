package com.wallet.backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "realized_credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RealizedCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "credit_id", nullable = false)
    private Credit credit;

    private double interestRate;   // taux d’intérêt (%)
    private double monthlyPayment; // paiement mensuel
    private double totalToPay;     // total à rembourser
    private LocalDate startDate;   // début du prêt
    private LocalDate endDate;     // fin du prêt
}
