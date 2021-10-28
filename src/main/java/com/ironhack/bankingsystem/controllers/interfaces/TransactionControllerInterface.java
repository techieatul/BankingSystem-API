package com.ironhack.bankingsystem.controllers.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.utils.*;

public interface TransactionControllerInterface {
    Money sendMoney(TransactionDTO transactionDTO);

}
