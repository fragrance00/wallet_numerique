package com.wallet.backend.service;

import com.wallet.backend.dto.*;
import com.wallet.backend.entities.*;
import com.wallet.backend.shared.Exceptions.ResourceNotFoundException;
import com.wallet.backend.interfaces.RealizedCreditService;
import com.wallet.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RealizedCreditServiceImpl implements RealizedCreditService {

    private final RealizedCreditRepository realizedCreditRepository;
    private final CreditRepository creditRepository;
    private final AccountRepository accountRepository;

    @Override
    public RealizedCreditResponseDTO createCredit(RealizedCreditRequestDTO request, Long accountId) {
        Credit credit = creditRepository.findById(request.getCreditId())
                .orElseThrow(() -> new ResourceNotFoundException("Crédit introuvable avec ID: " + request.getCreditId()));

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable avec ID: " + accountId));

        double amount = credit.getAmount();
        double interestRate = request.getInterestRate();
        int months = request.getMonths();

        double totalToPay = amount * (1 + (interestRate / 100));
        double monthlyPayment = totalToPay / months;

        RealizedCredit realizedCredit = RealizedCredit.builder()
                .credit(credit)
                .interestRate(interestRate)
                .monthlyPayment(monthlyPayment)
                .totalToPay(totalToPay)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(months))
                .build();

        realizedCreditRepository.save(realizedCredit);

        return mapToDTO(realizedCredit);
    }

    @Override
    public List<RealizedCreditResponseDTO> getAllRealizedCredits() {
        return realizedCreditRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RealizedCreditResponseDTO getRealizedCreditById(Long id) {
        RealizedCredit realizedCredit = realizedCreditRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Crédit réalisé introuvable avec ID: " + id));
        return mapToDTO(realizedCredit);
    }

    @Override
    public List<RealizedCreditResponseDTO> getRealizedCreditsByAccount(Long accountId) {
        return realizedCreditRepository.findByCredit_Account_Id(accountId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<RealizedCreditResponseDTO> getRealizedCreditsByBanker(Banker banker) {
        return realizedCreditRepository.findByCredit_Account_Banker(banker)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private RealizedCreditResponseDTO mapToDTO(RealizedCredit realizedCredit) {
        Account account = realizedCredit.getCredit().getAccount();
        Client client = account.getClient();

        return RealizedCreditResponseDTO.builder()
                .id(realizedCredit.getId())
                .interestRate(realizedCredit.getInterestRate())
                .monthlyPayment(realizedCredit.getMonthlyPayment())
                .totalToPay(realizedCredit.getTotalToPay())
                .startDate(realizedCredit.getStartDate())
                .endDate(realizedCredit.getEndDate())
                .clientFirstName(client.getFirstName())
                .clientLastName(client.getLastName())
                .accountRib(account.getAccountNumber())
                .build();
    }
}
