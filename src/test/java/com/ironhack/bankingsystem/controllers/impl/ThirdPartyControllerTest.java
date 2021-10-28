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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ThirdPartyControllerTest {

    @Autowired
    CheckingAccountRepository checkingAccountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @Autowired
    ThirdPartyTransactionRepository thirdPartyTransactionRepository;

    PasswordEncoder pwdEnconder = new BCryptPasswordEncoder();
    @Autowired
    ThirdPartyRepository thirdPartyRepository;
    AccountHolder accountHolder1;
    CheckingAccount checkingAccount;
    ThirdParty thirdParty;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    private User admin;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        admin = userRepository.save(new Admin("admin1234", "1234"));
        accountHolder1 = new AccountHolder("jasato", "1234", "Jaume Sánchez", LocalDate.of(1989, 1, 13).atStartOfDay(), new Address("Spain", "Madrid", "Callle Murillo", 10, "07006"));
        accountHolderRepository.save(accountHolder1);
        checkingAccount = new CheckingAccount(new Money(new BigDecimal("500")), pwdEnconder.encode("1234"), accountHolderRepository.findAll().get(0), null);
        thirdParty = new ThirdParty("Pedro", "abc123");
        checkingAccountRepository.save(checkingAccount);
        thirdPartyRepository.save(thirdParty);
    }

    @AfterEach
    void tearDown() {
        thirdPartyTransactionRepository.deleteAll();
        checkingAccountRepository.deleteAll();
        accountHolderRepository.deleteAll();
        thirdPartyRepository.deleteAll();
    }

    @Test
    void getAllThirdPartyAccounts() {

        assertEquals(1, thirdPartyRepository.findAll().size());


    }

    @Test
    void createThirdParty() throws Exception {
        ThirdParty thirdParty = new ThirdParty("Antonio", "1234");
        MvcResult result = mockMvc.perform(post("/admin/third-party-account/new")
                .with(user(new CustomUserDetails(admin)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(thirdParty)))
                .andExpect(status().isCreated()).andReturn();

    }

    @Test
    void sendMoney() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(
                thirdPartyRepository.findAll().get(0).getId(),
                new BigDecimal("10"),
                null,
                checkingAccountRepository.findAll().get(0).getAccountId(),
                checkingAccountRepository.findAll().get(0).getSecretKey()
        );

        MvcResult result = mockMvc.perform(post("/third-party/send-money")
                .header("hashedKey", "abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(thirdPartyTransactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("510.00"), checkingAccountRepository.findAll().get(0).getBalance().getAmount());
    }

    @Test
    void receiveMoney() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(
                thirdPartyRepository.findAll().get(0).getId(),
                new BigDecimal("10"),
                null,
                checkingAccountRepository.findAll().get(0).getAccountId(),
                checkingAccountRepository.findAll().get(0).getSecretKey()
        );

        MvcResult result = mockMvc.perform(post("/third-party/receive-money")
                .header("hashedKey", "abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(thirdPartyTransactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("490.00"), checkingAccountRepository.findAll().get(0).getBalance().getAmount());
    }

    @Test
    void receiveMoney_appliesFees() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(
                thirdPartyRepository.findAll().get(0).getId(),
                new BigDecimal("260"),
                null,
                checkingAccountRepository.findAll().get(0).getAccountId(),
                checkingAccountRepository.findAll().get(0).getSecretKey()
        );

        MvcResult result = mockMvc.perform(post("/third-party/receive-money")
                .header("hashedKey", "abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(thirdPartyTransactionDTO)))
                .andExpect(status().isOk()).andReturn();

        assertEquals(new BigDecimal("200.00"), checkingAccountRepository.findAll().get(0).getBalance().getAmount());
    }


    @Test
    void receiveMoney_notEnoughFunds() throws Exception {
        ThirdPartyTransactionDTO thirdPartyTransactionDTO = new ThirdPartyTransactionDTO(
                thirdPartyRepository.findAll().get(0).getId(),
                new BigDecimal("600"),
                null,
                checkingAccountRepository.findAll().get(0).getAccountId(),
                checkingAccountRepository.findAll().get(0).getSecretKey()
        );

        MvcResult result = mockMvc.perform(post("/third-party/receive-money")
                .header("hashedKey", "abc123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(thirdPartyTransactionDTO)))
                .andExpect(status().isForbidden()).andReturn();

    }
}