package com.ironhack.bankingsystem.models.users;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User{

    public Admin(@NotNull(message = "Username required") String username, @NotNull(message = "Password required") String password) {
        super(username, password);
        super.setRole("ADMIN");
    }

    public Admin() {
    }

}
