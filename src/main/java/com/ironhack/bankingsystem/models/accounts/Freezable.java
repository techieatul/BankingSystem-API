package com.ironhack.bankingsystem.models.accounts;

import com.ironhack.bankingsystem.enums.*;

public interface Freezable {

    Status getStatus();

    void setStatus(Status status);

    Long getAccountId();
}
