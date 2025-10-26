package com.wallet.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponse {
    private Long id;
    private double amount;
    private String type;
    private String timestamp;
    private String location; // Suppression de description
    private Long fromAccountId;
    private Long toAccountId;
    private String fromAccountNumber;
    private String toAccountNumber;
}