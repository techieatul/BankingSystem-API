package com.ironhack.bankingsystem.services.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.enums.*;
import com.ironhack.bankingsystem.models.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.repositories.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import com.ironhack.bankingsystem.utils.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.math.*;
import java.time.*;
import java.time.temporal.*;

@Service
public class TransactionService implements TransactionServiceInterface {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    StudentCheckingAccountRepository studentCheckingAccountRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    CheckingAccountRepository checkingAccountRepository;

    @Autowired
    CreditCardRepository creditCardRepository;

    @Autowired
    TransactionRepository transactionRepository;


    public Money transferMoney(UserDetails userDetails, TransactionDTO transactionDTO) {


        if (accountsArePresent(transactionDTO)) {

            Account senderAccount = getSenderAccount(transactionDTO);

            if (accountHasPermissions(senderAccount, userDetails)) {

                senderAccount = evaluateAccounts(senderAccount, transactionDTO);
                Account recipientAccount = evaluateAccounts(getRecipientAccount(transactionDTO), transactionDTO);
                makeTransaction(transactionDTO, senderAccount, recipientAccount);
                return transactionDTO.getTransactionAmount();

            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have rights to make transfers from this account");
            }
        } else {
            if (!accountRepository.findById(transactionDTO.getSenderAccountId()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, but the account you are trying to transfer funds from does not exist in the database");
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sorry, but the account you are trying to transfer funds to does not exist in the database");
            }
        }
    }

    private boolean accountsArePresent(TransactionDTO transactionDTO) {
        return accountRepository.findById(transactionDTO.getSenderAccountId()).isPresent()
                && accountRepository.findById(transactionDTO.getRecipientAccountId()).isPresent();
    }

    //This method takes the accounts in the transaction, checks its status if needed, checks possible fraud and if they have enough funds and returns the account
    private Account evaluateAccounts(Account account, TransactionDTO transactionDTO) {

        if (account instanceof CheckingAccount) {
            CheckingAccount checkingAccount = (CheckingAccount) account;
            checkStatus(checkingAccount);
            if (isSender(checkingAccount, transactionDTO)) {
                checkFraud(checkingAccount);
                if (!enoughFunds(checkingAccount, transactionDTO.getTransactionAmount().getAmount())) {
                    saveAndThrowException(checkingAccount);
                } else {
                    return checkingAccount;
                }
            }
            applyMonthlyFee(checkingAccount);
            checkBalanceAndApplyExtraFees(checkingAccount, transactionDTO);
            return checkingAccount;

        } else if (account instanceof StudentCheckingAccount) {
            StudentCheckingAccount studentCheckingAccount = (StudentCheckingAccount) account;
            checkStatus(studentCheckingAccount);
            if (isSender(studentCheckingAccount, transactionDTO)) {
                checkFraud(studentCheckingAccount);
                if (!enoughFunds(studentCheckingAccount, transactionDTO.getTransactionAmount().getAmount())) {
                    saveAndThrowException(studentCheckingAccount);
                } else {
                    return studentCheckingAccount;
                }
            } else {
                return studentCheckingAccount;
            }

        } else if (account instanceof CreditCard) {
            CreditCard creditCard = (CreditCard) account;
            applyInterestRate(creditCard);
            checkFraud(creditCard);


        } else if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            checkStatus(savingsAccount);
            applyInterestRate(savingsAccount);
            if (isSender(savingsAccount, transactionDTO)) {
                checkFraud(savingsAccount);
                if (!enoughFunds(savingsAccount, transactionDTO.getTransactionAmount().getAmount())) {
                    saveAndThrowException(savingsAccount);
                }
            }


        }
        return account;
    }

    public CreditCard applyInterestRate(CreditCard creditCard) {
        Long monthsBetween = ChronoUnit.MONTHS.between(creditCard.getLastInterestApplied(), LocalDateTime.now());
        if (monthsBetween > 0 && creditCard.getBalance().getAmount().compareTo(BigDecimal.ZERO) > 0) {
            creditCard.setBalance(
                    new Money(creditCard.getBalance().getAmount()
                            .multiply(new BigDecimal(monthsBetween))
                            .multiply(
                                    creditCard.getInterestRate()
                                            .divide(new BigDecimal("12")))));
            creditCard.setLastInterestApplied(LocalDateTime.now());
            creditCardRepository.save(creditCard);

        }

        return creditCard;
    }

    public SavingsAccount applyInterestRate(SavingsAccount savingsAccount) {
        Long yearsBetween = ChronoUnit.YEARS.between(savingsAccount.getLastInterestsApplied(), LocalDateTime.now());
        if (yearsBetween > 0 && savingsAccount.getBalance().getAmount().compareTo(savingsAccount.getMinimumBalance().getAmount()) > 0) {
            savingsAccount.setBalance(
                    new Money(savingsAccount.getBalance().getAmount()
                            .multiply(new BigDecimal(yearsBetween))
                            .multiply(
                                    savingsAccount.getInterestRate()
                            )));
            savingsAccount.setLastInterestsApplied(LocalDateTime.now());
            savingsAccountRepository.save(savingsAccount);

        }

        return savingsAccount;
    }


    private void makeTransaction(TransactionDTO transactionDTO, Account senderAccount, Account recipientAccount) {
        senderAccount.setBalance(new Money(senderAccount.getBalance().getAmount().subtract(transactionDTO.getTransactionAmount().getAmount()), senderAccount.getBalance().getCurrency()));
        recipientAccount.setBalance(new Money(recipientAccount.getBalance().getAmount().add(transactionDTO.getTransactionAmount().getAmount()), recipientAccount.getBalance().getCurrency()));
        transactionRepository.save(new Transaction(senderAccount, recipientAccount, transactionDTO.getTransactionAmount()));
    }


    //Determines if the account given is making the transaction
    private boolean isSender(Account account, TransactionDTO transactionDTO) {
        return account.getAccountId().equals(transactionDTO.getSenderAccountId());
    }

  //This method only takes accounts that have Status and evaluates if there has been fraud
    public void checkFraud(Freezable senderAccount) {
        //Retrieves the last transaction made in the last second (if exists)
        if (transactionRepository.findTransactionBySenderAndTimeStampBetween((Account) senderAccount, LocalDateTime.now().minusSeconds(1), LocalDateTime.now()).isPresent()) {
            senderAccount.setStatus(Status.FROZEN);
            saveAccount((Account) senderAccount);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction rejected: Your account is now frozen due to a potential fraud detected");
        }

        if (transactionRepository.getMaxByDay(senderAccount.getAccountId()).isPresent() && transactionRepository.getSumLastTransactions(senderAccount.getAccountId()).isPresent()) {

        //Compares the max sum of transactions in any day to the sum of the transactions in the last 24 hours
            BigDecimal getMaxByDay = transactionRepository.getMaxByDay(senderAccount.getAccountId()).get().multiply(new BigDecimal("1.5"));
            BigDecimal getLastDay = transactionRepository.getSumLastTransactions(senderAccount.getAccountId()).get();

            if (getMaxByDay.compareTo(getLastDay) < 0) {

                senderAccount.setStatus(Status.FROZEN);
                saveAccount((Account) senderAccount);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction rejected: Your account is now frozen due to a potential fraud detected");
            }

        }

    }

    //Overridden method that applies only to credit cards. It only evaluates if there was a transaction in the last second
    private void checkFraud(CreditCard creditCard) {
        if (transactionRepository.findTransactionBySenderAndTimeStampBetween((Account) creditCard, LocalDateTime.now().minusSeconds(1), LocalDateTime.now()).isPresent()) {
            saveAccount(creditCard);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Transaction rejected: You cannot transfer money now due to a potential fraud detected");

        }
    }

    private void saveAndThrowException(Account account) {
        saveAccount(account);
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, but the account you are trying to transfer funds from does not have enough funds to perform this transaction");
    }

    private void checkBalanceAndApplyExtraFees(Penalizable account, TransactionDTO transactionDTO) {
        if (account.getAccountId().equals(transactionDTO.getSenderAccountId())) {
            if (dropsBelowMinimumBalance(account, transactionDTO.getTransactionAmount().getAmount())) {
                if (!enoughFunds((Account) account, transactionDTO.getTransactionAmount().getAmount())) {
                    saveAccount((Account) account);
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, but the account you are trying to transfer funds from does not have enough funds to perform this transaction");
                } else {
                    applyPenaltyFee(account);
                }
            }
        }
    }

    public void checkBalanceAndApplyExtraFeesThirdParty(Penalizable account, BigDecimal amount) {

        if (dropsBelowMinimumBalance(account, amount)) {
            if (!enoughFunds((Account) account, amount)) {
                saveAccount((Account) account);
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Sorry, but the account you are trying to transfer funds from does not have enough funds to perform this transaction");
            } else {
                applyPenaltyFee(account);
            }
        }

    }


    public boolean enoughFunds(Account account, BigDecimal amount) {
        return account.getBalance().getAmount().subtract(amount).compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean dropsBelowMinimumBalance(Penalizable penalizable, BigDecimal amount) {
        return penalizable.getMinimumBalance().getAmount().compareTo(penalizable.getBalance().getAmount().subtract(amount)) > 0;
    }


    private Account applyPenaltyFee(Penalizable penalizable) {
        penalizable.setBalance(new Money(penalizable.getBalance().getAmount().subtract(Constants.PENALTY_FEE)));
        return (Account) penalizable;
    }

    public void applyMonthlyFee(CheckingAccount checkingAccount) {
        Long monthsBetween = ChronoUnit.MONTHS.between(checkingAccount.getMaintenanceFeeLastTimeApplied(), LocalDateTime.now());


        checkingAccount.setBalance(new Money(
                checkingAccount.getBalance().getAmount()
                        .subtract(checkingAccount.getMonthlyMaintenanceFee().getAmount().multiply(new BigDecimal(monthsBetween))),
                checkingAccount.getBalance().getCurrency()));

        checkingAccount.setMaintenanceFeeLastTimeApplied(LocalDateTime.now());
        saveAccount(checkingAccount);


    }

    public void checkStatus(Freezable savingsAccount) {
        if (savingsAccount.getStatus().equals(Status.FROZEN)) {
            saveAccount((Account) savingsAccount);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Account " + savingsAccount.getAccountId() + " is suspended");
        }

    }



    private void saveAccount(Account account) {
        if (account instanceof CheckingAccount) {
            checkingAccountRepository.save((CheckingAccount) account);
        } else if (account instanceof StudentCheckingAccount) {
            studentCheckingAccountRepository.save((StudentCheckingAccount) account);
        } else if (account instanceof CreditCard) {
            creditCardRepository.save((CreditCard) account);
        } else if (account instanceof SavingsAccount) {
            savingsAccountRepository.save((SavingsAccount) account);
        }

    }


    private Account getRecipientAccount(TransactionDTO transactionDTO) {
        return accountRepository.findById(transactionDTO.getRecipientAccountId()).get();
    }

    private Account getSenderAccount(TransactionDTO transactionDTO) {
        return accountRepository.findById(transactionDTO.getSenderAccountId()).get();
    }



    private boolean accountHasPermissions(Account senderAccount, UserDetails userDetails) {

        return senderAccount.getAccountHolder().getUsername().equals(userDetails.getUsername()) ||
                senderAccount.getSecondaryAccountHolder().getUsername().equals(userDetails.getUsername());

    }
}
