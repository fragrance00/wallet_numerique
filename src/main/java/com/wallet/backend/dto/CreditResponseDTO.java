package com.wallet.backend.dto;

import com.wallet.backend.entities.Client;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditResponseDTO {

    private Long id;
    private double amount;
    private String purpose;
    private String documents;
    private LocalDateTime requestDate;
    private String status;

    // Informations du compte
    private Long accountNumber;
    private String clientFirstName;
    private String clientLastName;
}
