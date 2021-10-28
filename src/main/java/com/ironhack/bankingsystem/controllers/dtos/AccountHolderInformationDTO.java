package com.ironhack.bankingsystem.controllers.dtos;

import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.utils.*;

import java.time.*;
import java.util.*;

public class AccountHolderInformationDTO {

    private String username;
    private String name;
    private LocalDateTime dateOfBirth;
    private Address primaryAddress;
    private Address mailingAddress;

    public AccountHolderInformationDTO() {
    }

    public AccountHolderInformationDTO(String username, String name, LocalDateTime dateOfBirth, Address primaryAddress, Address mailingAddress) {
        this.username = username;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
}
