package com.ironhack.bankingsystem.services.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import com.ironhack.bankingsystem.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.util.*;

@Service
public class AccountService implements AccountServiceInterface {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountHolderRepository accountHolderRepository;
    @Autowired
    TransactionService transactionService;

    public AccountInfoDTO getAccountById(Long id) {
        if (accountRepository.findById(id).isPresent()) {
            return convertAccountIntoDTO(accountRepository.findById(id).get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " doesn't exist in the database");
        }

    }


    public Money getBalanceById(Long id) {

        if (accountRepository.findById(id).isPresent()) {
            return accountRepository.findById(id).get().getBalance();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + id + " doesn't exist in the database");
        }

    }


    public List<AccountInfoDTO> getAllAccountsFromUser(Long userId) {

        if (accountHolderRepository.findById(userId).isPresent()) {
            List<AccountInfoDTO> accounts = new ArrayList<>();
            for (Account account : accountHolderRepository.findById(userId).get().getPrimaryAccounts()) {
                accounts.add(convertAccountIntoDTO(account));
            }
            for (Account account : accountHolderRepository.findById(userId).get().getSecondaryAccounts()) {
                accounts.add(convertAccountIntoDTO(account));
            }
            return accounts;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " doesn't exist in the database");
        }
    }

    public void updateBalance(Long accountId, Money money) {

        if (accountRepository.findById(accountId).isPresent()) {

            Account account = accountRepository.findById(accountId).get();
            account.setBalance(money);
            accountRepository.save(account);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + accountId + " doesn't exist in the database");
        }


    }

    public Money getBalance(Long accountId, UserDetails userDetails) {

        if (accountRepository.findById(accountId).isPresent()) {
            Account account = accountRepository.findById(accountId).get();
            if (account.getAccountHolder().getUsername().equals(userDetails.getUsername()) || account.getSecondaryAccountHolder().getUsername().equals(userDetails.getUsername())) {
                if (account instanceof CreditCard) transactionService.applyInterestRate((CreditCard) account);
                if (account instanceof SavingsAccount) transactionService.applyInterestRate((SavingsAccount) account);
                return account.getBalance();
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permissions for this account");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id " + accountId + " doesn't exist in the database");

        }
    }

    private AccountInfoDTO convertAccountIntoDTO(Account account) {

        if (account.getSecondaryAccountHolder() == null) {
            return new AccountInfoDTO(
                    account.getAccountId(),
                    account.getBalance(),
                    new AccountHolderInformationDTO(
                            account.getAccountHolder().getUsername(),
                            account.getAccountHolder().getName(),
                            account.getAccountHolder().getDateOfBirth(),
                            account.getAccountHolder().getPrimaryAddress(),
                            account.getAccountHolder().getMailingAddress()),
                    null,
                    account.getCreationDate()
            );

        } else {
            return new AccountInfoDTO(
                    account.getAccountId(),
                    account.getBalance(),
                    new AccountHolderInformationDTO(
                            account.getAccountHolder().getUsername(),
                            account.getAccountHolder().getName(),
                            account.getAccountHolder().getDateOfBirth(),
                            account.getAccountHolder().getPrimaryAddress(),
                            account.getAccountHolder().getMailingAddress()),

                    new AccountHolderInformationDTO(
                            account.getSecondaryAccountHolder().getUsername(),
                            account.getSecondaryAccountHolder().getName(),
                            account.getSecondaryAccountHolder().getDateOfBirth(),
                            account.getSecondaryAccountHolder().getPrimaryAddress(),
                            account.getSecondaryAccountHolder().getMailingAddress()),
                    account.getCreationDate()
            );

        }


    }
}
