package com.ironhack.bankingsystem.controllers.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.models.users.*;

import java.util.*;

public interface AccountHolderControllerInterface {

    List<AccountHolderInformationDTO> getAllAccountHolders();
    AccountHolder createAccountHolder (AccountHolderDTO accountHolder);



}
