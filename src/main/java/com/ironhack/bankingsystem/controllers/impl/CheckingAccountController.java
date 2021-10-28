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
public class CheckingAccountController implements CheckingAccountControllerInterface {

    @Autowired
    CheckingAccountServiceInterface checkingAccountService;

    @GetMapping("/admin/checking-accounts")
    public List<CheckingAccount> getAllCheckingAccounts() {
        return checkingAccountService.getAllCheckingAccounts();
    }

    @PostMapping("/admin/checking-account/new/")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createCheckingAccount(@RequestBody @Valid CheckingAccountDTO checkingAccount) {

        return checkingAccountService.createCheckingAccount(checkingAccount);
    }

    @PostMapping("/admin/checking-account/{id}")
    public CheckingAccount updateCheckingAccount(@PathVariable Long id, @RequestBody CheckingAccount checkingAccount) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return checkingAccountService.updateCheckingAccount(id, checkingAccount);
    }
}
