package com.ironhack.bankingsystem.controllers.dtos;

import javax.validation.constraints.*;
import java.math.*;
import java.util.*;

public class ThirdPartyTransactionDTO {
    @NotNull
    Long thirdPartyId;
    private String hashedKey;
    @NotNull
    @DecimalMin("0")
    private BigDecimal amount;
    private Currency currency;
    @NotNull
    private Long accountId;
    @NotNull
    private String secretKey;

    public ThirdPartyTransactionDTO(@NotNull Long thirdPartyId, String hashedKey,  @NotNull @DecimalMin("0") BigDecimal amount, Currency currency, @NotNull Long accountId, @NotNull String secretKey) {
        this.thirdPartyId = thirdPartyId;
        this.amount = amount;
        this.currency = currency == null ? Currency.getInstance("USD") : currency;
        this.accountId = accountId;
        this.secretKey = secretKey;
    }

    public ThirdPartyTransactionDTO(@NotNull Long thirdPartyId, @NotNull @DecimalMin("0") BigDecimal amount, Currency currency, @NotNull Long accountId, @NotNull String secretKey) {
        this.thirdPartyId = thirdPartyId;
        this.amount = amount;
        this.currency = currency;
        this.accountId = accountId;
        this.secretKey = secretKey;
    }

    public ThirdPartyTransactionDTO() {
    }

    public Long getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(Long thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
