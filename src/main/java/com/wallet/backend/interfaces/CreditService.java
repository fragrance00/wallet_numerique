package com.wallet.backend.interfaces;

import com.wallet.backend.dto.CreditRequestDTO;
import com.wallet.backend.dto.CreditResponseDTO;
import com.wallet.backend.entities.Banker;
import com.wallet.backend.entities.Status;
import java.util.List;

public interface CreditService {

    CreditResponseDTO createCredit(CreditRequestDTO creditRequest, Long accountId);
    List<CreditResponseDTO> getAllCredits();
    CreditResponseDTO getCreditById(Long id);
    CreditResponseDTO updateCreditStatus(Long id, Status status);
    List<CreditResponseDTO> getCreditsByStatus(Status status);
    List<CreditResponseDTO> getCreditsByAccount(Long accountId);
    void deleteCredit(Long id);
    List<CreditResponseDTO> getCreditsByBanker(Banker banker);
}
