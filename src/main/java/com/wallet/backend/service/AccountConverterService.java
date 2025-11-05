package com.wallet.backend.service;

import com.wallet.backend.dto.AccountDto;
import com.wallet.backend.entities.Account;
import org.springframework.stereotype.Service;

@Service
public class AccountConverterService {

    public AccountDto convertToDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(account.getId());
        dto.setAccountNumber(formatRib(account.getAccountNumber()));
        dto.setBalance(account.getBalance());
        return dto;
    }

    private String formatRib(Long accountNumber) {
        String rib = accountNumber.toString();
        if (rib.length() < 27) {
            rib = String.format("%27s", rib).replace(' ', '0');
        }
        return "FR76 " + rib.substring(0, 4) + " " + rib.substring(4, 8) + " " +
                rib.substring(8, 12) + " " + rib.substring(12, 16) + " " +
                rib.substring(16, 20) + " " + rib.substring(20, 23) + " " + rib.substring(23);
    }
}