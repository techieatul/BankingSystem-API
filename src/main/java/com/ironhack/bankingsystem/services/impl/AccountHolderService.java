package com.ironhack.bankingsystem.services.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.time.*;
import java.time.format.*;
import java.util.*;

@Service
public class AccountHolderService implements AccountHolderServiceInterface {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<AccountHolderInformationDTO> getAllAccountHolders() {

        List<AccountHolderInformationDTO> result = new ArrayList<>();

        for (AccountHolder accountHolder : accountHolderRepository.findAll()) {
            result.add(new AccountHolderInformationDTO(
                    accountHolder.getUsername(),
                    accountHolder.getName(),
                    accountHolder.getDateOfBirth(),
                    accountHolder.getPrimaryAddress(),
                    accountHolder.getMailingAddress()));
        }

        return result;
    }

    public AccountHolder createAccountHolder(AccountHolderDTO accountHolder) {

        LocalDateTime localDateTime;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(accountHolder.getDateOfBirth(), dateTimeFormatter);
            localDateTime = LocalDateTime.of(localDate, LocalTime.of(0, 0));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format: valid date format is : dd-mm-yyyy");

        }


        if (accountHolderRepository.findByUsername(accountHolder.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user with this username already exists in the database");
        } else {
            return accountHolderRepository.save(new AccountHolder(
                    accountHolder.getUsername(),
                    passwordEncoder.encode(accountHolder.getPassword()),
                    accountHolder.getName(),
                    localDateTime,
                    accountHolder.getPrimaryAddress(),
                    accountHolder.getMailingAddress()
            ));
        }

    }

    public AccountHolder updateDetails(Long id, AccountHolder account) {

        if (accountHolderRepository.findById(id).isPresent()) {
            account.setId(id);
            return accountHolderRepository.save(account);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A user with id " + id + "doesn't exist in the database");

        }
    }
}
