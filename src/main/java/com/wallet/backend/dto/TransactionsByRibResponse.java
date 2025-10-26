package com.wallet.backend.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class TransactionsByRibResponse {
    private List<TransactionResponse> transactionsEnvoyees;
    private List<TransactionResponse> transactionsRecues;
    private int totalEnvoyees;
    private int totalRecues;

    public TransactionsByRibResponse(List<TransactionResponse> envoyees, List<TransactionResponse> recues) {
        this.transactionsEnvoyees = envoyees;
        this.transactionsRecues = recues;
        this.totalEnvoyees = envoyees.size();
        this.totalRecues = recues.size();
    }
}