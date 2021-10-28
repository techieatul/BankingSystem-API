package com.ironhack.bankingsystem.models.users;

import com.fasterxml.jackson.annotation.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.utils.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.*;
import javax.validation.constraints.*;
import java.time.*;
import java.util.*;

@Entity
@Table(name = "account_holders")
@PrimaryKeyJoinColumn(name = "id")
public class AccountHolder extends User{

    private LocalDateTime dateOfBirth;
    private String name;
    @Valid
    @Embedded
    private Address primaryAddress;
    @Embedded
    @Valid
    @AttributeOverrides({
            @AttributeOverride(name = "country", column = @Column(name = "mail_address_country")),
            @AttributeOverride(name = "city", column = @Column(name = "mail_address_city")),
            @AttributeOverride(name = "streetName", column = @Column(name = "mail_address_street")),
            @AttributeOverride(name = "number", column = @Column(name = "mail_address_number")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "mail_address_zip_code"))
    })
    private Address mailingAddress;

    @JsonIgnore
    @OneToMany( mappedBy = "accountHolder")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Account> primaryAccounts;
    @JsonIgnore
    @OneToMany(mappedBy = "secondaryAccountHolder")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Account> secondaryAccounts;

    public AccountHolder() {
        super.setRole("ACCOUNT_HOLDER");

    }

    public AccountHolder(String username,  String password,  String name, LocalDateTime dateOfBirth, Address address) {
        super(username, password);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = address;
        this.name = name;
        super.setRole("ACCOUNT_HOLDER");
    }

    public AccountHolder( String username, String password,  String name,  LocalDateTime dateOfBirth,  Address primaryAddress, @Valid Address mailingAddress) {
        super(username, password);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
        this.name = name;
        super.setRole("ACCOUNT_HOLDER");


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

    public List<Account> getPrimaryAccounts() {
        return primaryAccounts;
    }

    public void setPrimaryAccounts(List<Account> primaryAccounts) {
        this.primaryAccounts = primaryAccounts;
    }

    public List<Account> getSecondaryAccounts() {
        return secondaryAccounts;
    }

    public void setSecondaryAccounts(List<Account> secondaryAccounts) {
        this.secondaryAccounts = secondaryAccounts;
    }


}
