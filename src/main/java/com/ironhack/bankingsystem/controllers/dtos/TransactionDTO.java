package com.ironhack.bankingsystem.controllers.dtos;

import com.ironhack.bankingsystem.utils.*;
import org.springframework.http.*;
import org.springframework.web.server.*;

import javax.validation.constraints.*;
import java.math.*;
import java.util.*;

public class TransactionDTO {

    private Long senderAccountId;
    private Long recipientAccountId;
    private String recipientName;
    private Money transactionAmount;

    public TransactionDTO(Long senderAccountId, Long recipientId, String recipientName, @DecimalMin(value = "0.01", message = "Amount must be above 0") BigDecimal amount, @Pattern(regexp = "(\\w{3})", message = "Please provide a valid currency") String currency) {
        try {
            this.senderAccountId = senderAccountId;
            this.recipientAccountId = recipientId;
            this.recipientName = recipientName;
            this.transactionAmount = new Money(amount, Currency.getInstance(currency.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide a valid currency");
        }

    }

    public TransactionDTO() {
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(Long senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public Long getRecipientAccountId() {
        return recipientAccountId;
    }

    public void setRecipientAccountId(Long recipientAccountId) {
        this.recipientAccountId = recipientAccountId;
    }

    public Money getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(Money transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
