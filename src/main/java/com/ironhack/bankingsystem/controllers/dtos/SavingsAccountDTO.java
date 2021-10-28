package com.ironhack.bankingsystem.controllers.dtos;

import com.ironhack.bankingsystem.enums.*;
import com.ironhack.bankingsystem.utils.*;

import javax.validation.constraints.*;
import java.math.*;
import java.util.*;

public class SavingsAccountDTO {

    @NotNull
    @DecimalMin("0")
    private BigDecimal balance;
    private Currency currency;
    @NotNull
    private String secretKey;
    @NotNull
    private Long accountHolderId;
    private Long secondaryAccountHolderId;
    @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5")
    @DecimalMin(value = "0", message = "Interest rate must be above 0 or 0")
    private BigDecimal interestRate;
    @DecimalMax(value = "1000", message = "Minimum balance must be below 0.5")
    @DecimalMin(value = "100", message = "Minimum balance must be above 0 or 0")
    private BigDecimal minimumBalance;

    public SavingsAccountDTO(@NotNull @DecimalMin("0") BigDecimal balance, Currency currency, @NotNull String secretKey, @NotNull Long accountHolderId, Long secondaryAccountHolderId, @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5") @DecimalMin(value = "0", message = "Interest rate must be above 0 or 0") BigDecimal interestRate, @DecimalMax(value = "1000", message = "Minimum balance must be below 0.5") @DecimalMin(value = "100", message = "Minimum balance must be above 0 or 0") BigDecimal minimumBalance) {
        this.balance = balance;
        this.currency = currency == null ? Currency.getInstance("USD") : currency;
        this.secretKey = secretKey;
        this.accountHolderId = accountHolderId;
        this.secondaryAccountHolderId = secondaryAccountHolderId;
        this.interestRate = interestRate == null ? Constants.SAVINGS_ACC_DEFAULT_INTEREST_RATE : interestRate;;
        this.minimumBalance = minimumBalance == null ? Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE : minimumBalance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public Long getSecondaryAccountHolderId() {
        return secondaryAccountHolderId;
    }

    public void setSecondaryAccountHolderId(Long secondaryAccountHolderId) {
        this.secondaryAccountHolderId = secondaryAccountHolderId;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}
