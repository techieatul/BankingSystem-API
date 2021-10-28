package com.ironhack.bankingsystem.controllers.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.users.*;

import java.util.*;

public interface ThirdPartyControllerInterface {

    List<ThirdParty> getAllThirdPartyAccounts();
    ThirdParty createThirdParty(ThirdParty thirdParty);
    void sendMoney (String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);
    void receiveMoney(String hashedKey, ThirdPartyTransactionDTO thirdPartyTransactionDTO);

}
