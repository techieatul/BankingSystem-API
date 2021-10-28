package com.ironhack.bankingsystem.services.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.models.users.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import com.ironhack.bankingsystem.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.util.*;

@Service
public class ThirdPartyService implements ThirdPartyServiceInterface {

    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ThirdPartyTransactionRepository thirdPartyTransactionRepository;


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ThirdParty createThirdParty(ThirdParty thirdParty) {

        thirdParty.setHashedKey(passwordEncoder.encode(thirdParty.getHashedKey()));
        return thirdPartyRepository.save(thirdParty);
    }


    public List<ThirdParty> getAllThirdPartyAccounts() {
        return thirdPartyRepository.findAll();
    }

    public void sendMoney(ThirdPartyTransactionDTO thirdPartyTransactionDTO) {

        Account account = evaluateAccounts(thirdPartyTransactionDTO);
        account.setBalance(new Money(account.getBalance().getAmount().add(thirdPartyTransactionDTO.getAmount()), account.getBalance().getCurrency()));
        if (account instanceof CheckingAccount) transactionService.applyMonthlyFee((CheckingAccount) account);
        if (account instanceof CreditCard) transactionService.applyInterestRate((CreditCard) account);
        if (account instanceof SavingsAccount) transactionService.applyInterestRate((SavingsAccount) account);
        accountRepository.save(account);
        thirdPartyTransactionRepository.save(new ThirdPartyTransaction(
                account,
                thirdPartyRepository.findById(thirdPartyTransactionDTO.getThirdPartyId()).get(),
                new Money(thirdPartyTransactionDTO.getAmount(), thirdPartyTransactionDTO.getCurrency() == null ? Currency.getInstance("USD") : thirdPartyTransactionDTO.getCurrency())
        ));


    }

    public void receiveMoney(ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        Account account = evaluateAccounts(thirdPartyTransactionDTO);

        if (transactionService.enoughFunds(account, thirdPartyTransactionDTO.getAmount())) {
            if (account instanceof Freezable) {
                transactionService.checkStatus((Freezable) account);
                transactionService.checkFraud((Freezable) account);
            }
            if (account instanceof Penalizable) {
                transactionService.checkBalanceAndApplyExtraFeesThirdParty((Penalizable) account, thirdPartyTransactionDTO.getAmount());
            }

            if (account instanceof CheckingAccount) transactionService.applyMonthlyFee((CheckingAccount) account);
            if (account instanceof CreditCard) transactionService.applyInterestRate((CreditCard) account);
            if (account instanceof SavingsAccount) transactionService.applyInterestRate((SavingsAccount) account);

            account.setBalance(new Money(account.getBalance().getAmount().subtract(thirdPartyTransactionDTO.getAmount()), account.getBalance().getCurrency()));
            accountRepository.save(account);
            thirdPartyTransactionRepository.save(new ThirdPartyTransaction(
                    account,
                    thirdPartyRepository.findById(thirdPartyTransactionDTO.getThirdPartyId()).get(),
                    new Money(thirdPartyTransactionDTO.getAmount().negate(), thirdPartyTransactionDTO.getCurrency() == null ? Currency.getInstance("USD") : thirdPartyTransactionDTO.getCurrency())
            ));


        } else {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not enough funds");


        }

    }

    private Account evaluateAccounts(ThirdPartyTransactionDTO thirdPartyTransactionDTO) {
        if (accountRepository.findById(thirdPartyTransactionDTO.getAccountId()).isPresent() && thirdPartyRepository.findById(thirdPartyTransactionDTO.getThirdPartyId()).isPresent()) {
            Account account = accountRepository.findById(thirdPartyTransactionDTO.getAccountId()).get();
            ThirdParty thirdParty = thirdPartyRepository.findById(thirdPartyTransactionDTO.getThirdPartyId()).get();
            if (account.getSecretKey().equals(thirdPartyTransactionDTO.getSecretKey()) && thirdParty.getHashedKey().equals(thirdPartyTransactionDTO.getHashedKey())) {
                return account;
            } else if (!account.getSecretKey().equals(thirdPartyTransactionDTO.getSecretKey())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong Secret Key");
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Wrong Hashed key");

            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, but we couldn't find the account or the third party to make this transaction");

        }
    }
}
