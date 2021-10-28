package com.ironhack.bankingsystem.controllers.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.controllers.interfaces.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import javax.validation.constraints.*;
import java.util.*;

@RestController
public class ThirdPartyController implements ThirdPartyControllerInterface {

    @Autowired
    ThirdPartyServiceInterface thirdPartyService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/admin/third-party-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<ThirdParty> getAllThirdPartyAccounts() {
        return thirdPartyService.getAllThirdPartyAccounts();
    }

    @PostMapping("/admin/third-party-account/new")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty createThirdParty(@RequestBody @Valid ThirdParty thirdParty) {
        return thirdPartyService.createThirdParty(thirdParty);
    }

    @PostMapping("/third-party/send-money")
    @ResponseStatus(HttpStatus.OK)
    public void sendMoney(@RequestHeader @NotNull String hashedKey, @RequestBody @Valid ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        thirdPartyTransactionDTO.setHashedKey(hashedKey);
        thirdPartyService.sendMoney(thirdPartyTransactionDTO);

    }

    @PostMapping("/third-party/receive-money")
    @ResponseStatus(HttpStatus.OK)
    public void receiveMoney(@RequestHeader @NotNull String hashedKey,@RequestBody @Valid ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        thirdPartyTransactionDTO.setHashedKey(hashedKey);
        thirdPartyService.receiveMoney(thirdPartyTransactionDTO);

    }

}
