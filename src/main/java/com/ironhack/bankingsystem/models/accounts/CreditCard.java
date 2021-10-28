package com.ironhack.bankingsystem.models.accounts;

import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.utils.*;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;
import java.math.*;
import java.time.*;
import java.util.*;

@Entity
@PrimaryKeyJoinColumn(name = "accountId")
@Table(name = "credit_card")
public class CreditCard  extends Account{
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency")),
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount"))
    })
    private Money creditLimit;

    private BigDecimal interestRate;

    private LocalDateTime lastInterestApplied;


    public CreditCard() {
    }

    public CreditCard(Money balance, String secretKey,  @NotNull @Valid AccountHolder accountHolder, @Valid AccountHolder secondaryAccountHolder, Money creditLimit,  BigDecimal interestRate) {
        super(balance, secretKey, accountHolder, secondaryAccountHolder);
        setCreditLimit(creditLimit);
        setInterestRate(interestRate);
        lastInterestApplied = LocalDateTime.now();


    }

    public LocalDateTime getLastInterestApplied() {
        return lastInterestApplied;
    }

    public void setLastInterestApplied(LocalDateTime lastInterestApplied) {
        this.lastInterestApplied = lastInterestApplied;
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(Money creditLimit) {

        this.creditLimit = creditLimit == null ? new Money(new BigDecimal("100")) : creditLimit;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {

        this.interestRate = interestRate == null ? new BigDecimal("0.12") : interestRate;
    }

}
