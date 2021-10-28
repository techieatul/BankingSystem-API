package com.ironhack.bankingsystem.controllers.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.controllers.interfaces.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import com.ironhack.bankingsystem.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;

@RestController
public class TransactionController implements TransactionControllerInterface {

    @Autowired
    TransactionServiceInterface transactionService;

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public Money sendMoney(@RequestBody @Valid TransactionDTO transactionDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();

        return transactionService.transferMoney(userDetails, transactionDTO);
    }


}

