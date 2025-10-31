package com.wallet.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreditRequestDTO {

    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private Double amount;

    @NotBlank(message = "L'objet du crédit (purpose) est obligatoire")
    private String purpose;

    @NotBlank(message = "Le document est obligatoire")
    private String documents;
}
