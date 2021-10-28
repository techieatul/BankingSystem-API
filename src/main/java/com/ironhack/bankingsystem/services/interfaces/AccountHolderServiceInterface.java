package com.ironhack.bankingsystem.services.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.users.*;

import java.util.*;

public interface AccountHolderServiceInterface {

    List<AccountHolderInformationDTO> getAllAccountHolders();
    AccountHolder createAccountHolder (AccountHolderDTO accountHolder);
    AccountHolder updateDetails(Long id, AccountHolder account);



}
