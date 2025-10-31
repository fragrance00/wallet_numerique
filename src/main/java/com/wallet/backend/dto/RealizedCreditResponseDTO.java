package com.wallet.backend.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RealizedCreditResponseDTO {
    private Long id;
    private double interestRate;
    private double monthlyPayment;
    private double totalToPay;
    private LocalDate startDate;
    private LocalDate endDate;

    private String clientFirstName;
    private String clientLastName;
    private Long accountRib;
}
