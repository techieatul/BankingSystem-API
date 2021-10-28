package com.ironhack.bankingsystem.services.impl;

import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.repositories.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

@Service
public class UserRetrieveService {
    @Autowired
    AccountHolderRepository accountHolderRepository;

    public AccountHolder retrieveUser(Long id) {

        if (!accountHolderRepository.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Checking account with id " + id + " doesn't in the database");
        } else {
            return accountHolderRepository.findById(id).get();
        }

    }

}
