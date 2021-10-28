package com.ironhack.bankingsystem.controllers.dtos;

import javax.validation.constraints.*;

public class AccountNameDTO {
    @NotEmpty(message = "Account name can't be empty")
    private String name;
    public AccountNameDTO(String name) {
        this.name = name.replace("-", " ");
    }

    public AccountNameDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
