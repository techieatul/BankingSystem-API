package com.ironhack.bankingsystem.controllers.impl;

import com.ironhack.bankingsystem.controllers.dtos.*;
import com.ironhack.bankingsystem.controllers.interfaces.*;
import com.ironhack.bankingsystem.models.accounts.*;
import com.ironhack.bankingsystem.services.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;
import java.util.*;

@RestController
public class StudentCheckingAccountController implements StudentCheckingAccountControllerInterface {

    @Autowired
    StudentCheckingAccountServiceInterface studentAccountService;


    @GetMapping("/admin/student-checking-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<StudentCheckingAccount> getAllStudentCheckingAccounts() {
        return studentAccountService.getAllStudentCheckingAccounts();
    }

    @PostMapping("/admin/student-checking-account/new")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentCheckingAccount createStudentCheckingAccount(@RequestBody @Valid CheckingAccountDTO studentCheckingAccount) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return studentAccountService.createStudentCheckingAccount(studentCheckingAccount);
    }

    @PatchMapping("/admin/student-checking-account/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudentCheckingAccount updateStudentCheckingAccount(@PathVariable("id") Long id, @RequestBody @Valid StudentCheckingAccount studentCheckingAccount) {

        return studentAccountService.updateStudentCheckingAccount(id, studentCheckingAccount);
    }
}
