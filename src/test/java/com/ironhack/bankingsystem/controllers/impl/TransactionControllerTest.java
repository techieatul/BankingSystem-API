package com.ironhack.bankingsystem.controllers.impl;

import com.fasterxml.jackson.databind.*;
import com.ironhack.bankingsystem.controllers.impl.*;
import com.ironhack.bankingsystem.models.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.utils.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.math.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
public
class TransactionControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionController transactionController;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    CreditCardRepository creditCardRepository;
    PasswordEncoder pwdEnconder = new BCryptPasswordEncoder();
    AccountHolder accountHolder1;
    AccountHolder accountHolder2;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        //@NotNull(message = "Username required") String username, @NotNull(message = "Password required") String password, @NotBlank(message = "Name required") String name, @NotNull(message = "Date of birth required") LocalDateTime dateOfBirth, @Valid @NotNull(message = "Address required") Address address
        accountHolder1 = new AccountHolder("jasato", pwdEnconder.encode("1234"), "Jaume Sanchez", LocalDate.of(1989, 1, 13).atStartOfDay(), new Address("Spain", "Madrid", "Callle Murillo", 10, "07006"));
        accountHolder2 = new AccountHolder("jasato2", pwdEnconder.encode("12345"), "Jose Perez", LocalDate.of(2000, 1, 15).atStartOfDay(), new Address("Spain", "Barcelona", "Las Ramblas", 10, "47890"));
        accountHolderRepository.saveAll(List.of(accountHolder1, accountHolder2));
        //    public CheckingAccount(Money balance, String secretKey, @NotNull @Valid AccountHolder accountHolder, @Valid AccountHolder secondaryAccountHolder, Money minimumBalance, Money monthlyMaintenanceFee) {
        CheckingAccount account1 = new CheckingAccount(new Money(new BigDecimal("500")), pwdEnconder.encode("1234"), accountHolderRepository.findAll().get(0), null);
        CheckingAccount account2 = new CheckingAccount(new Money(new BigDecimal("10000")), pwdEnconder.encode("1234"), accountHolderRepository.findAll().get(1), null);
        CheckingAccount account3 = new CheckingAccount(new Money(new BigDecimal("480")), pwdEnconder.encode("1234"), accountHolder1, null);
        //    public CreditCard(Money balance, String secretKey,  @NotNull @Valid AccountHolder accountHolder, @Valid AccountHolder secondaryAccountHolder, Money creditLimit,  BigDecimal interestRate)
        CreditCard creditCard1 = new CreditCard(new Money(new BigDecimal("500")), pwdEnconder.encode("1234"), accountHolderRepository.findAll().get(0), null, null, null);
        checkingAccountRepository.saveAll(List.of(account1, account2, account3));
        creditCardRepository.save(creditCard1);

    }

    @AfterEach
    void tearDown() {

        transactionRepository.deleteAll();
        accountRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        userRepository.deleteAll();


    }

    @Test
    void selectSumDays_Work() throws Exception {

        Transaction transaction = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction.setTimeStamp(LocalDateTime.now().minusDays(10));
        Transaction transaction2 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction2.setTimeStamp(LocalDateTime.now().minusMonths(1));
        Transaction transaction3 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction3.setTimeStamp(LocalDateTime.now().minusMonths(1));
        Transaction transaction4 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction4.setTimeStamp(LocalDate.of(2020, 01,13).atStartOfDay().plusHours(3));
        Transaction transaction5 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction5.setTimeStamp(LocalDate.of(2020, 01,13).atStartOfDay().plusHours(1));
        Transaction transaction6 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction6.setTimeStamp(LocalDate.of(2020, 01,13).atStartOfDay().plusHours(2));
        transactionRepository.saveAll(List.of(transaction, transaction2, transaction3, transaction4, transaction5, transaction6));

        assertEquals(new BigDecimal("30.00"), transactionRepository.getMaxByDay(checkingAccountRepository.findAll().get(0).getAccountId()).get());


    }
   @Test
    void selectSumLastHours_Work() throws Exception {

        Transaction transaction = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction.setTimeStamp(LocalDateTime.now().minusHours(22));
        Transaction transaction2 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction2.setTimeStamp(LocalDateTime.now().minusHours(2));
        Transaction transaction3 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        Transaction transaction4 = new Transaction(checkingAccountRepository.findAll().get(1), checkingAccountRepository.findAll().get(0), new Money(new BigDecimal("10")));
        transaction3.setTimeStamp(LocalDateTime.now().minusHours(1));

        transactionRepository.saveAll(List.of(transaction, transaction2, transaction3, transaction4));

        assertEquals(new BigDecimal("30.00"), transactionRepository.getSumLastTransactions(checkingAccountRepository.findAll().get(0).getAccountId()).get());


    }

}
