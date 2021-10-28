package com.ironhack.bankingsystem.repositories;

import com.ironhack.bankingsystem.models.*;
import com.ironhack.bankingsystem.models.accounts.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.time.*;
import java.util.*;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTransactionBySenderAndTimeStampBetween(Account sender, LocalDateTime secondAgo, LocalDateTime now);

    @Query(value = "SELECT SUM(t.amount) FROM bankingsys.transactions t WHERE sender_account_id = :id AND  t.time_stamp < date_sub(now(), interval 24 HOUR) GROUP BY DATE(t.time_stamp) ORDER BY 1 DESC LIMIT 1", nativeQuery = true)
    Optional<BigDecimal> getMaxByDay(@Param("id") Long id);

    @Query(value = "SELECT SUM(t.amount) FROM bankingsys.transactions t WHERE sender_account_id = :id AND t.time_stamp > date_sub(now(), interval 24 HOUR) LIMIT 1", nativeQuery = true)
    Optional<BigDecimal> getSumLastTransactions(@Param("id") Long id);
}
