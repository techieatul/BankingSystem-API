package com.ironhack.bankingsystem.repositories;

import com.ironhack.bankingsystem.models.users.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.*;

@Repository
public interface ThirdPartyRepository extends JpaRepository<ThirdParty, Long> {
}
