package com.ironhack.bankingsystem.controllers.dtos;

import com.ironhack.bankingsystem.enums.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.utils.*;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;
import java.math.*;
import java.time.*;
import java.util.*;

public class CheckingAccountDTO {

    @NotNull
    @DecimalMin("0")
    private BigDecimal balance;
    private Currency currency;
    @NotNull
    private String secretKey;
    @NotNull
    private Long accountHolderId;
    private Long secondaryAccountHolderId;


    public CheckingAccountDTO(@NotNull @DecimalMin("0") BigDecimal balance, Currency currency, String secretKey, @NotNull Long accountHolderId, Long secondaryAccountHolderId) {
        this.balance = balance;
        setCurrency(currency);
        this.secretKey = secretKey;
        this.accountHolderId = accountHolderId;
        this.secondaryAccountHolderId = secondaryAccountHolderId;

    }



    public BigDecimal getBalance() {
        return balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency =  currency == null ?  Currency.getInstance("USD") : currency;
    }

    public Long getAccountHolderId() {
        return accountHolderId;
    }

    public void setAccountHolderId(Long accountHolderId) {
        this.accountHolderId = accountHolderId;
    }

    public Long getSecondaryAccountHolderId() {
        return secondaryAccountHolderId;
    }

    public void setSecondaryAccountHolderId(Long secondaryAccountHolderId) {
        this.secondaryAccountHolderId = secondaryAccountHolderId;
    }

}
