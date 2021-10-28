package com.ironhack.bankingsystem.utils;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Embeddable
public class Address {
    @NotBlank(message = "Country required")
    private String country;
    @NotBlank(message = "City required")
    private String city;
    @NotBlank(message = "Str. name required")
    private String streetName;
    @Digits(integer = 4, fraction = 0, message = "Valid street number required")
    @Min(1)
    private int number;
    @Pattern(regexp="(\\d{5})", message = "Only valid Spanish zip numbers are accepted")
    @NotBlank(message = "Zip code required")
    private String zipCode;

    public Address() {
    }

    public Address(@NotBlank(message = "Country required") String country, @NotBlank(message = "City required") String city, @NotBlank(message = "Str. name required") String streetName, @Digits(integer = 4, fraction = 0, message = "Valid street number required") @Min(1) int number, @Pattern(regexp = "(\\d{5})", message = "Only valid Spanish zip numbers are accepted") @NotBlank(message = "Zip code required") String zipCode) {
        this.country = country;
        this.city = city;
        this.streetName = streetName;
        this.number = number;
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
