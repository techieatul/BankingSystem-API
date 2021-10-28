package com.ironhack.bankingsystem.repositories;

import com.ironhack.bankingsystem.models.accounts.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
