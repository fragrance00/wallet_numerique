package com.wallet.backend.entities;

// Enum pour le statut d'une demande de crédit
public enum Status {
    PENDING,    // en attente de validation
    APPROVED,   // validé par le banquier
    REJECTED,   // refusé
    REALIZED
}
