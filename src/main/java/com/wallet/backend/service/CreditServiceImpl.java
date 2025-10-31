package com.wallet.backend.service;

import com.wallet.backend.dto.CreditRequestDTO;
import com.wallet.backend.dto.CreditResponseDTO;
import com.wallet.backend.entities.*;
import com.wallet.backend.interfaces.CreditService;
import com.wallet.backend.repository.AccountRepository;
import com.wallet.backend.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private final CreditRepository creditRepository;
    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    public CreditServiceImpl(CreditRepository creditRepository, AccountRepository accountRepository) {
        this.creditRepository = creditRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public CreditResponseDTO createCredit(CreditRequestDTO creditRequest, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Compte introuvable pour l'ID: " + accountId));

        Credit credit = Credit.builder()
                .amount(creditRequest.getAmount())
                .purpose(creditRequest.getPurpose())
                .documents(creditRequest.getDocuments())
                .status(Status.PENDING)
                .requestDate(LocalDateTime.now())
                .account(account)
                .build();

        Credit savedCredit = creditRepository.save(credit);
        return mapToDTO(savedCredit);
    }

    @Override
    public List<CreditResponseDTO> getAllCredits() {
        return creditRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public CreditResponseDTO getCreditById(Long id) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Crédit introuvable avec ID: " + id));
        return mapToDTO(credit);
    }

    @Override
    public CreditResponseDTO updateCreditStatus(Long id, Status status) {
        Credit credit = creditRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Crédit introuvable avec ID: " + id));
        credit.setStatus(status);
        Credit updated = creditRepository.save(credit);
        return mapToDTO(updated);
    }

    @Override
    public List<CreditResponseDTO> getCreditsByStatus(Status status) {
        return creditRepository.findByStatus(status)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<CreditResponseDTO> getCreditsByAccount(Long accountId) {
        return creditRepository.findByAccountId(accountId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void deleteCredit(Long id) {
        if (!creditRepository.existsById(id)) {
            throw new IllegalArgumentException("Crédit introuvable avec ID: " + id);
        }
        creditRepository.deleteById(id);
    }

    private CreditResponseDTO mapToDTO(Credit credit) {
        return CreditResponseDTO.builder()
                .id(credit.getId())
                .amount(credit.getAmount())
                .purpose(credit.getPurpose())
                .documents(credit.getDocuments())
                .requestDate(credit.getRequestDate())
                .status(credit.getStatus().name())
                .accountNumber(credit.getAccount().getAccountNumber())
                .clientFirstName(credit.getAccount().getClient().getFirstName())
                .clientLastName(credit.getAccount().getClient().getLastName())
                .build();
    }

    @Override
    public List<CreditResponseDTO> getCreditsByBanker(Banker banker) {
        return creditRepository.findByBanker(banker)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
}
