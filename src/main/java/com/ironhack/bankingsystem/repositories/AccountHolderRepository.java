package com.ironhack.bankingsystem.repositories;

import com.ironhack.bankingsystem.models.users.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder, Long> {

    Optional<AccountHolder> findByUsername(String username);
}
