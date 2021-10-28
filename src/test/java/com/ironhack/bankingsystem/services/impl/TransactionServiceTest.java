package com.ironhack.bankingsystem.services.impl;

import com.fasterxml.jackson.databind.*;
import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.controllers.impl.*;
import com.ironhack.bankingsystem.enums.*;
import com.ironhack.bankingsystem.models.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.security.*;
import com.ironhack.bankingsystem.utils.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.*;
import org.springframework.web.context.*;

import java.math.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class TransactionServiceTest {
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
    Transaction transaction;
    Transaction transaction2;
    Transaction transaction3;


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

        transaction = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction.setTimeStamp(LocalDateTime.now().minusMonths(1).minusMinutes(1));
        transaction2 = new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction2.setTimeStamp(LocalDateTime.now().minusMonths(1).minusMinutes(3));
        transaction3= new Transaction(checkingAccountRepository.findAll().get(0), checkingAccountRepository.findAll().get(1), new Money(new BigDecimal("10")));
        transaction3.setTimeStamp(LocalDateTime.now().minusMinutes(3));
        transactionRepository.saveAll(List.of(transaction, transaction2, transaction3));

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
    void sendMoney() throws Exception {

        //Long senderAccountId, Long recipientId, String recipientName, BigDecimal amount,  String currency
        TransactionDTO transactionDTO = new TransactionDTO(checkingAccountRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("50"), "USD");


        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("450.00"), checkingAccountRepository.findAll().get(0).getBalance().getAmount());
        assertEquals(new BigDecimal("10050.00"), checkingAccountRepository.findAll().get(1).getBalance().getAmount());

    }

    @Test
    void appliesFees() throws Exception {

        CheckingAccount checkingAccount = checkingAccountRepository.findAll().get(0);
        checkingAccount.setMaintenanceFeeLastTimeApplied(LocalDateTime.now().minusMonths(1));
        checkingAccountRepository.save(checkingAccount);

        TransactionDTO transactionDTO = new TransactionDTO(checkingAccountRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("50"), "USD");

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("438.00"), checkingAccountRepository.findAll().get(0).getBalance().getAmount());
        assertEquals(new BigDecimal("10050.00"), checkingAccountRepository.findAll().get(1).getBalance().getAmount());

    }

    @Test
    void appliesFees_AppliesExtraFees() throws Exception {

        CheckingAccount checkingAccount = checkingAccountRepository.findAll().get(0);
        checkingAccount.setBalance(new Money(new BigDecimal("250")));
        checkingAccountRepository.save(checkingAccount);

        TransactionDTO transactionDTO = new TransactionDTO(checkingAccountRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("50"), "USD");

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("160.00"), checkingAccountRepository.findAll().get(0).getBalance().getAmount());
        assertEquals(new BigDecimal("10050.00"), checkingAccountRepository.findAll().get(1).getBalance().getAmount());

    }

    @Test
    void appliesFees_notEnoughFunds() throws Exception {

        CheckingAccount checkingAccount = checkingAccountRepository.findAll().get(0);
        checkingAccount.setBalance(new Money(new BigDecimal("250")));
        checkingAccountRepository.save(checkingAccount);

        TransactionDTO transactionDTO = new TransactionDTO(checkingAccountRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("300"), "USD");

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isForbidden()).andReturn();
        assertEquals(new BigDecimal("250.00"), checkingAccountRepository.findAll().get(0).getBalance().getAmount());
        assertEquals(new BigDecimal("10000.00"), checkingAccountRepository.findAll().get(1).getBalance().getAmount());

    }

    @Test
    void appliesInterestsCreditCard() throws Exception {
        CreditCard creditCard = creditCardRepository.findAll().get(0);
        creditCard.setLastInterestApplied(LocalDateTime.now().minusMonths(12));
        creditCardRepository.save(creditCard);

        TransactionDTO transactionDTO = new TransactionDTO(creditCardRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("100"), "USD");

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("460.00"), creditCardRepository.findAll().get(0).getBalance().getAmount());

    }

    @Test
    void appliesInterestsCreditCard_sixMonths() throws Exception {
        CreditCard creditCard = creditCardRepository.findAll().get(0);
        creditCard.setLastInterestApplied(LocalDateTime.now().minusMonths(6));
        creditCardRepository.save(creditCard);

        TransactionDTO transactionDTO = new TransactionDTO(creditCardRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("100"), "USD");

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("430.00"), creditCardRepository.findAll().get(0).getBalance().getAmount());

    }

    @Test
    void appliesInterestsCreditCard_noInterests() throws Exception {
        CreditCard creditCard = creditCardRepository.findAll().get(0);


        TransactionDTO transactionDTO = new TransactionDTO(creditCardRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("100"), "USD");

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("400.00"), creditCardRepository.findAll().get(0).getBalance().getAmount());

    }

    @Test
    void checkFraud_twoTransactionsInLessThanOneSecond() throws Exception {
        CreditCard creditCard = creditCardRepository.findAll().get(0);
        creditCard.setLastInterestApplied(LocalDateTime.now().minusMonths(12));
        creditCardRepository.save(creditCard);

        TransactionDTO transactionDTO = new TransactionDTO(creditCardRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("100"), "USD");
        TransactionDTO transactionDTO2 = new TransactionDTO(creditCardRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("100"), "USD");

        mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO2)))
                .andExpect(status().isForbidden()).andReturn();


    }

    @Test
    void checkFraud_twoTransactionsInLessThanOneSecond_CheckingAccount() throws Exception {
        CheckingAccount checkingAccount = checkingAccountRepository.findAll().get(0);
        checkingAccountRepository.save(checkingAccount);

        TransactionDTO transactionDTO = new TransactionDTO(checkingAccountRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("100"), "USD");
        TransactionDTO transactionDTO2 = new TransactionDTO(checkingAccountRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("100"), "USD");

        mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO2)))
                .andExpect(status().isForbidden()).andReturn();

    }


    @Test
    void checkFraud_moreSpentInLast24Hours() throws Exception {


        TransactionDTO transactionDTO = new TransactionDTO(checkingAccountRepository.findAll().get(0).getAccountId(), checkingAccountRepository.findAll().get(1).getAccountId(), "Jose Perez", new BigDecimal("50"), "USD");


        MvcResult result = mockMvc.perform(post("/transfer")
                .with(user(new CustomUserDetails(accountHolder1)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isOk()).andReturn();

    }


}
