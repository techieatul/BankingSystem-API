package com.ironhack.bankingsystem.controllers.impl;

import com.fasterxml.jackson.databind.*;
import com.ironhack.bankingsystem.controllers.dtos.*;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
class CheckingAccountControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
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
    User admin;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        admin = userRepository.save(new Admin("admin1234", pwdEncoder.encode("1234")));
        accountHolder1 = new AccountHolder("jasato", pwdEnconder.encode("1234"), "Jaume Sanchez", LocalDate.of(1989, 1, 13).atStartOfDay(), new Address("Spain", "Madrid", "Callle Murillo", 10, "07006"));
        accountHolderRepository.save(accountHolder1);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();

    }

    @Test
    void getAllCheckingAccounts() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/checking-accounts")
                .with(user(new CustomUserDetails(admin)))).andExpect(status().isOk()).andReturn();
    }

    @Test
    void createCheckingAccount() throws Exception {

        CheckingAccountDTO checkingAccountDTO = new CheckingAccountDTO(
                new BigDecimal("200"),
                Currency.getInstance("USD"),
                "1234",
                accountHolderRepository.findAll().get(0).getId(),
                null
        );

        MvcResult result = mockMvc.perform(post("/admin/checking-account/new/")
                .with(user(new CustomUserDetails(admin)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(checkingAccountDTO)))
                .andExpect(status().isCreated()).andReturn();
    }

}

