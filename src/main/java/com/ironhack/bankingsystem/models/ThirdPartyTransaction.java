package com.ironhack.bankingsystem.models;

import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.utils.*;

import javax.persistence.*;
import java.time.*;

@Entity
@Table(name = "third_party_transaction")
public class ThirdPartyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "third_party_id")
    ThirdParty thirdParty;
    private LocalDateTime timeStamp;
    private Money amount;

    public ThirdPartyTransaction(Account account, ThirdParty thirdParty, Money amount) {

        this.account = account;
        this.thirdParty = thirdParty;
        this.amount = amount;
        timeStamp = LocalDateTime.now();
    }

    public ThirdPartyTransaction() {
        timeStamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public ThirdParty getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(ThirdParty thirdParty) {
        this.thirdParty = thirdParty;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }
}
