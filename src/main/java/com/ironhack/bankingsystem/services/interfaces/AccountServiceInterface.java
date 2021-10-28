package com.ironhack.bankingsystem.services.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.utils.*;
import org.springframework.security.core.userdetails.*;

import java.util.*;

public interface AccountServiceInterface {
    AccountInfoDTO getAccountById(Long id);
    Money getBalanceById(Long id);
    List<AccountInfoDTO> getAllAccountsFromUser(Long userId);
    void updateBalance(Long accountId, Money money);
    // TODO
    Money getBalance(Long accountId, UserDetails userDetails);

}
