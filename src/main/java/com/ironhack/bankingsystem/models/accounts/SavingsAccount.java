package com.ironhack.bankingsystem.models.accounts;

import com.ironhack.bankingsystem.enums.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.utils.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.*;
import javax.validation.constraints.*;
import java.math.*;
import java.time.*;

@Entity
@PrimaryKeyJoinColumn(name = "accountId")
@Table(name = "savings_account")
public class SavingsAccount extends Account implements Freezable, Penalizable {
    private BigDecimal interestRate;
    private Money minimumBalance;
    private Status status;
    private LocalDateTime lastInterestsApplied;

    public SavingsAccount() {status = Status.ACTIVE;}

    public SavingsAccount(Money balance, String secretKey,  @NotNull @Valid AccountHolder accountHolder, @Valid AccountHolder secondaryAccountHolder, @DecimalMax(value = "0.5", message = "Interest rate must be below 0.5") @DecimalMin(value = "0", message = "Interest rate must be above 0 or 0") BigDecimal interestRate, Money minimumBalance) {
        super(balance, secretKey, accountHolder, secondaryAccountHolder);
        setInterestRate(interestRate);
        setMinimumBalance(minimumBalance);
        lastInterestsApplied = LocalDateTime.now();

    }

    public LocalDateTime getLastInterestsApplied() {
        return lastInterestsApplied;
    }

    public void setLastInterestsApplied(LocalDateTime lastTimeInterestsApplied) {
        this.lastInterestsApplied = lastTimeInterestsApplied;
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance == null ? new Money(Constants.SAVINGS_ACC_DEFAULT_MIN_BALANCE) : minimumBalance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {

        this.interestRate = interestRate == null ? Constants.SAVINGS_ACC_DEFAULT_INTEREST_RATE : interestRate;;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
