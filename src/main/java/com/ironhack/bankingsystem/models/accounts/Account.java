package com.ironhack.bankingsystem.models.accounts;

import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.utils.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.*;
import javax.validation.constraints.*;
import java.time.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private Money balance;
    private String secretKey;
    @ManyToOne
    @NotNull
    @Valid
    @JoinColumn(name = "account_holder")
    private AccountHolder accountHolder;
    @ManyToOne
    @Valid
    @JoinColumn(name = "secondary_account_holder")
    private AccountHolder secondaryAccountHolder;
    private LocalDateTime creationDate;

    public Account(Money balance, String secretKey, @NotNull @Valid AccountHolder accountHolder, @Valid AccountHolder secondaryAccountHolder) {
        this.balance = balance;
        this.secretKey = secretKey;
        this.accountHolder = accountHolder;
        this.secondaryAccountHolder = secondaryAccountHolder;
        this.creationDate = LocalDateTime.now();
    }

    public Account(){creationDate = LocalDateTime.now();}


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long id) {
        this.accountId = id;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public AccountHolder getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(AccountHolder accountHolder) {
        this.accountHolder = accountHolder;
    }

    public AccountHolder getSecondaryAccountHolder() {
        return secondaryAccountHolder;
    }

    public void setSecondaryAccountHolder(AccountHolder secondaryAccountHolder) {
        this.secondaryAccountHolder = secondaryAccountHolder;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

}
