package com.ironhack.bankingsystem.controllers.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.controllers.interfaces.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.services.impl.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import com.ironhack.bankingsystem.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
public class AccountController implements AccountControllerInterface {

    @Autowired
    AccountServiceInterface accountService;

    //Only accessed by ADMIN
    @GetMapping("/admin/account/id/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountInfoDTO getAccountById(@PathVariable("id") Long id) {
        return accountService.getAccountById(id);
    }


    @GetMapping("/admin/account/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public Money getBalanceById(@PathVariable("id") Long id) {
        return accountService.getBalanceById(id);
    }



    @GetMapping("/admin/account/{id}/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<AccountInfoDTO> getAllAccountsFromUser(@PathVariable("id") Long userId) {
        return accountService.getAllAccountsFromUser(userId);
    }

    @PostMapping("/admin/account/{id}/balance")
    @ResponseStatus(HttpStatus.OK)
    public void updateBalance(@PathVariable("id") Long accountId,@RequestBody @Valid Money money) {
        accountService.updateBalance(accountId, money);

    }

    @GetMapping("/my-accounts/{id}/balance")
    public Money getBalance(@PathVariable("id") Long accountId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        System.out.println(userDetails.getUsername());
       return accountService.getBalance(accountId, userDetails);

    }

}
