package com.ironhack.bankingsystem.controllers.impl;

import com.fasterxml.jackson.databind.*;
import com.ironhack.bankingsystem.controllers.dtos.*;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AccountHolderControllerTest {

    PasswordEncoder pwdEncoder = new BCryptPasswordEncoder();

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    AccountHolder accountHolder1;
    AccountHolderDTO accountHolderDTO;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private User admin;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        admin = userRepository.save(new Admin("admin1234", pwdEncoder.encode("1234")));
        accountHolder1 = new AccountHolder("jasato", pwdEncoder.encode("1234"), "Jaume Sánchez", LocalDate.of(1989, 1, 13).atStartOfDay(), new Address("Spain", "Madrid", "Callle Murillo", 10, "07006"));
        accountHolderRepository.save(accountHolder1);
        Account account1 = new Account(new Money(new BigDecimal("250"), Currency.getInstance("EUR")), pwdEncoder.encode("1234"), accountHolder1, null);
        accountRepository.save(account1);
    }

    @AfterEach
    void tearDown() {

        accountRepository.deleteAll();
        accountHolderRepository.deleteAll();

    }

    @Test
    void getAllAccountHolders() throws Exception {
        MvcResult result = mockMvc.perform(get("/admin/account-holders")
                .with(user(new CustomUserDetails(admin)))).andExpect(status().isOk()).andReturn();
    }

    @Test
    void createAccountHolder() throws Exception {

        accountHolderDTO = new AccountHolderDTO("jasato123123", "123123",
                "13-01-1989", "Jaume Sánchez", new Address("Calle Murillo", "Madrid", "Sapin", 14, "05632"), new Address("Calle Murillo", "Madrid", "Sapin", 14, "05632"));


        MvcResult result = mockMvc.perform(post("/admin/create-account-holder")
                .with(user(new CustomUserDetails(admin)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountHolderDTO)))
                .andExpect(status().isCreated()).andReturn();
    }

    @Test
    void createAccountHolder_wrongDateFormat() throws Exception {

        accountHolderDTO = new AccountHolderDTO("jasato123123", "123123",
                "13/01/1989", "Jaume Sánchez", new Address("Calle Murillo", "Madrid", "Sapin", 14, "05632"), new Address("Calle Murillo", "Madrid", "Sapin", 14, "05632"));


        MvcResult result = mockMvc.perform(post("/admin/create-account-holder")
                .with(user(new CustomUserDetails(admin)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountHolderDTO)))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void createAccountHolder_noAddress_throwsException() throws Exception {

        accountHolderDTO = new AccountHolderDTO("jasato123123", "123123",
                "13/01/1989", "Jaume Sánchez", null, new Address("Calle Murillo", "Madrid", "Sapin", 14, "05632"));


        MvcResult result = mockMvc.perform(post("/admin/create-account-holder")
                .with(user(new CustomUserDetails(admin)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountHolderDTO)))
                .andExpect(status().isBadRequest()).andReturn();
    }



}