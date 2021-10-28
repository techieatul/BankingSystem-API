package com.ironhack.bankingsystem.services.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.users.*;

import java.util.*;

public interface ThirdPartyServiceInterface {

    ThirdParty createThirdParty(ThirdParty thirdParty);
    List<ThirdParty> getAllThirdPartyAccounts();
    void sendMoney(ThirdPartyTransactionDTO thirdPartyTransactionDTO);
    void receiveMoney(ThirdPartyTransactionDTO thirdPartyTransactionDTO);
}
