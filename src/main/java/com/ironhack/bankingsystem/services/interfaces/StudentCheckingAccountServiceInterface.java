package com.ironhack.bankingsystem.services.interfaces;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.models.accounts.*;

import java.util.*;

public interface StudentCheckingAccountServiceInterface {
    StudentCheckingAccount createStudentCheckingAccount(CheckingAccountDTO studentCheckingAccount);
    StudentCheckingAccount updateStudentCheckingAccount(Long id, StudentCheckingAccount studentCheckingAccount);

    List<StudentCheckingAccount> getAllStudentCheckingAccounts();
}
