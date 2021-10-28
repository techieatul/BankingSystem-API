package com.ironhack.bankingsystem.controllers.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.controllers.interfaces.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
public class AccountHolderController implements AccountHolderControllerInterface {

    @Autowired
    AccountHolderServiceInterface accountHolderService;

    @GetMapping("/admin/account-holders")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountHolderInformationDTO> getAllAccountHolders() {
        return accountHolderService.getAllAccountHolders();
    }

    @PostMapping("/admin/create-account-holder")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountHolder createAccountHolder(@RequestBody AccountHolderDTO accountHolder) {
        return accountHolderService.createAccountHolder(accountHolder);
    }


}
