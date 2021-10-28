package com.ironhack.bankingsystem.controllers.dtos;

import javax.validation.*;
import javax.validation.constraints.*;
import java.math.*;
import java.util.*;

public class CreditCardDTO {
    @Valid
    private Currency currency;
    @DecimalMax(value = "10000", message = "Max credit limit must be below 10000")
    @DecimalMin(value = "100", message = "Min credit limit must be above 100")
    private BigDecimal creditLimit;
    @DecimalMin(value = "0.1", message = "Min interest rate must be above 0.1")
    private BigDecimal interestRate = new BigDecimal("0.1");
    @NotNull
    @DecimalMin(value = "0", message = "Minimum Balance must be zero or above zero")
    private BigDecimal balance;
    @NotNull
    private String secretKey;
    @NotNull
    @Valid
    private Long accountHolderId;
    @Valid
    private Long secondaryAccountHolderId;

    public CreditCardDTO(@Valid Currency currency, @DecimalMax(value = "10000", message = "Max credit limit must be below 10000") @DecimalMin(value = "100", message = "Min credit limit must be above 100") BigDecimal creditLimit, @DecimalMax(value = "0.2", message = "Max interest rate must be below 0.2") @DecimalMin(value = "0.1", message = "Min interest rate must be above 0.1") BigDecimal interestRate, @NotNull @DecimalMin(value = "0", message = "Minimum Balance must be zero or above zero") BigDecimal balance, @NotNull String secretKey, @NotNull @Valid Long accountHolder, Long secondaryAccountHolder) {
        this.currency = currency == null ? Currency.getInstance("USD") : currency;
        this.creditLimit = creditLimit == null?  new BigDecimal("100") : creditLimit;;
        this.interestRate = interestRate == null?  new BigDecimal("0.1") : interestRate;
        this.balance = balance;
        this.secretKey = secretKey;
        this.accountHolderId = accountHolder;
        this.secondaryAccountHolderId = secondaryAccountHolder;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getSecondaryAccountHolderId() {
        return secondaryAccountHolderId;
    }

    public void setSecondaryAccountHolderId(Long secondaryAccountHolderId) {
        this.secondaryAccountHolderId = secondaryAccountHolderId;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getAccountHolderId() {
        return accountHolderId;
    }

    public void setAccountHolderId(Long accountHolderId) {
        this.accountHolderId = accountHolderId;
    }
}


