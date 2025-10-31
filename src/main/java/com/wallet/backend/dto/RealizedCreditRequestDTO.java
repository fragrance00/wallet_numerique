package com.wallet.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RealizedCreditRequestDTO {

    @NotNull(message = "L'ID du crédit est obligatoire")
    private Long creditId;

    @NotNull(message = "Le taux d'intérêt est obligatoire")
    @Min(value = 1, message = "Le taux d'intérêt doit être supérieur à 0")
    private Double interestRate;

    @NotNull(message = "Le nombre de mois est obligatoire")
    @Min(value = 1, message = "La durée (mois) doit être au moins 1")
    private Integer months;
}
