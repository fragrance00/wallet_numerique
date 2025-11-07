package com.wallet.backend.interfaces;

import com.wallet.backend.dto.*;
import com.wallet.backend.entities.Banker;
import java.util.List;

public interface RealizedCreditService {
    RealizedCreditResponseDTO createCredit(RealizedCreditRequestDTO creditRequest);
    List<RealizedCreditResponseDTO> getAllRealizedCredits();
    RealizedCreditResponseDTO getRealizedCreditById(Long id);
    List<RealizedCreditResponseDTO> getRealizedCreditsByAccount(Long accountId);
    List<RealizedCreditResponseDTO> getRealizedCreditsByBanker(Banker banker);
}
