package com.ironhack.bankingsystem.models;

import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.utils.*;

import javax.persistence.*;
import java.time.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;
    @ManyToOne
    @JoinColumn(name = "recipient_account_id")
    private Account recipitent;
    private LocalDateTime timeStamp;
    private Money amount;

    public Transaction() {
        timeStamp = LocalDateTime.now();
    }

    public Transaction(Account senderId, Account recipientId,  Money amount) {
        this.sender = senderId;
        this.recipitent = recipientId;
        this.amount = amount;
        timeStamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getRecipitent() {
        return recipitent;
    }

    public void setRecipitent(Account recipitent) {
        this.recipitent = recipitent;
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
