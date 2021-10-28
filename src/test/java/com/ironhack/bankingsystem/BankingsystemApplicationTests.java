package com.ironhack.bankingsystem;

import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.utils.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.*;
import java.time.*;
import java.util.*;

@SpringBootTest
class BankingsystemApplicationTests {

    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

/*
    @BeforeEach
    void setUp() {

        //    public AccountHolder(@NotNull(message = "Username required") String username, @NotNull(message = "Password required") String password, @NotBlank(message = "Name required") String name, @NotNull(message = "Date of birth required") LocalDateTime dateOfBirth, @Valid @NotNull(message = "Address required") Address address) {
        //     public Address(@NotBlank(message = "Country required") String country, @NotBlank(message = "City required") String city, @NotBlank(message = "Str. name required") String streetName, @Digits(integer = 4, fraction = 0, message
        //     = "Valid street number required") @Min(1) int number, @Pattern(regexp = "(\\d{5})", message = "Only valid Spanish zip numbers are accepted") @NotBlank(message = "Zip code required") String zipCode) {
        AccountHolder accountHolder = new AccountHolder(
                "jasato", "12345", "Jaume Sánchez", LocalDateTime.of(1989,1,13,0,0),
                new Address("Spain", "Madrid","Calle Murillo",10, "07006"));
        accountHolderRepository.save(accountHolder);

        //     public CheckingAccount(Long id, Money balance, String secretKey, @NotNull AccountHolder accountHolder, Money minimumBalance, Money monthlyMaintenanceFee) {
        CheckingAccount checkingAccount = new CheckingAccount(1l, new Money(BigDecimal.valueOf(100), Currency.getInstance("EUR")),"123456", accountHolderRepository.findAll().get(0), new Money(BigDecimal.valueOf(10), Currency.getInstance("EUR")), new Money(BigDecimal.valueOf(10), Currency.getInstance("EUR")));
        checkingAccountRepository.save(checkingAccount);

    }

    @AfterEach
    void tearDown() {
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();


    }*/

    @Test
    void contextLoads() {
    }

}
