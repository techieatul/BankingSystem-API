package com.ironhack.bankingsystem.repositories;

import com.ironhack.bankingsystem.models.*;
import com.ironhack.bankingsystem.models.accounts.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;


@Repository
public interface ThirdPartyTransactionRepository extends JpaRepository<ThirdPartyTransaction, Long> {

}
