package com.ironhack.bankingsystem.models.accounts;

import com.ironhack.bankingsystem.enums.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.utils.*;

import javax.persistence.*;
import javax.validation.*;
import javax.validation.constraints.*;

@Entity
@PrimaryKeyJoinColumn(name = "accountId")
@Table(name = "student_checking_account")
public class StudentCheckingAccount extends Account implements Freezable {
    @Enumerated
    private Status status;

    public StudentCheckingAccount(){status = Status.ACTIVE;}

    public StudentCheckingAccount(Money balance, String secretKey,  @NotNull @Valid AccountHolder accountHolder, @Valid AccountHolder secondaryAccountHolder) {
        super(balance, secretKey, accountHolder, secondaryAccountHolder);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
