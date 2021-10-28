package com.ironhack.bankingsystem.repositories;

import com.ironhack.bankingsystem.models.accounts.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountHolderName(String name);
    Optional<Account> findBySecondaryAccountHolderName(String name);
    List<Account> findAccountsByAccountHolderId(Long id);
    List<Account> findAccountsBySecondaryAccountHolderId(Long id);
}
