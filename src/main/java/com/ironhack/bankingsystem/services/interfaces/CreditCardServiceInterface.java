package com.ironhack.bankingsystem.services.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;

import java.util.*;

public interface CreditCardServiceInterface {

    CreditCard createCreditCardAccount(CreditCardDTO creditCard);
    CreditCard updateCreditCardAccount(Long id, CreditCard creditCard);

    List<CreditCard> getAllCreditCards();
}
