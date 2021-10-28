package com.ironhack.bankingsystem.services.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;

import java.util.*;

public interface CheckingAccountServiceInterface {
    Account createCheckingAccount(CheckingAccountDTO checkingAccount);
    CheckingAccount updateCheckingAccount(Long id, CheckingAccount checkingAccount);

    List<CheckingAccount> getAllCheckingAccounts();
}
