package com.ironhack.bankingsystem.models.accounts;

import com.ironhack.bankingsystem.models.*;
import com.ironhack.bankingsystem.utils.*;

public interface Penalizable {

    Long getAccountId();

    Money getMinimumBalance();

    Money getBalance();

    void setBalance(Money money);
}
