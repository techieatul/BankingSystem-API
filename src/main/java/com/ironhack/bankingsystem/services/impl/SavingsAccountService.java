package com.ironhack.bankingsystem.services.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import com.ironhack.bankingsystem.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.util.*;

@Service
public class SavingsAccountService implements SavingsAccountServiceInterface {

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    UserRetrieveService userRetrieveService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SavingsAccount createSavingsAccount(SavingsAccountDTO savingsAccountDTO) {

            return savingsAccountRepository.save(new SavingsAccount(
                    new Money(savingsAccountDTO.getBalance(), savingsAccountDTO.getCurrency()),
                    passwordEncoder.encode(savingsAccountDTO.getSecretKey()),
                    userRetrieveService.retrieveUser(savingsAccountDTO.getAccountHolderId()),
                    savingsAccountDTO.getSecondaryAccountHolderId() == null ? null : userRetrieveService.retrieveUser(savingsAccountDTO.getSecondaryAccountHolderId()),
                    savingsAccountDTO.getInterestRate(),
                    new Money(savingsAccountDTO.getMinimumBalance(), savingsAccountDTO.getCurrency())

            ));

    }

    public SavingsAccount updateSavingsAccount(Long id, SavingsAccount savingsAccount) {
        if (savingsAccountRepository.findById(id).isPresent()) {
            savingsAccount.setAccountId(savingsAccountRepository.findById(id).get().getAccountId());
            return savingsAccountRepository.save(savingsAccount);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Savings Account with id " + id + " doesn't in the database");

        }
    }

    public List<SavingsAccount> getAllSavingsAccounts() {
        return savingsAccountRepository.findAll();
    }

}
