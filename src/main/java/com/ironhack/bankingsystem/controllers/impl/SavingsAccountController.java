package com.ironhack.bankingsystem.controllers.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.controllers.interfaces.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
public class SavingsAccountController implements SavingsAccountControllerInterface {

    @Autowired
    SavingsAccountServiceInterface savingsAccountService;


    @GetMapping("/admin/savings-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<SavingsAccount> getAllSavingsAccount() {
        return savingsAccountService.getAllSavingsAccounts();
    }

    @PostMapping("/admin/savings-account/new")

    @ResponseStatus(HttpStatus.CREATED)
    public SavingsAccount createSavingsAccount(@RequestBody @Valid SavingsAccountDTO savingsAccount) {

        return savingsAccountService.createSavingsAccount(savingsAccount);
    }

    @PostMapping("/admin/savings-account/{id}")
    public SavingsAccount updateSavingsAccount(@PathVariable Long id, @RequestBody SavingsAccount savingsAccount) {

        return savingsAccountService.updateSavingsAccount(id, savingsAccount);
    }
}
