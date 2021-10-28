package com.ironhack.bankingsystem.controllers.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.utils.*;

import java.util.*;

public interface AccountControllerInterface {
    AccountInfoDTO getAccountById(Long id);

    Money getBalanceById(Long id);

    List<AccountInfoDTO> getAllAccountsFromUser(Long userId);

    void updateBalance(Long accountId, Money money);

    Money getBalance(Long id);


}
