package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId AND"
        +
        " t.date BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsByUserAndDateRange(
        @Param("userId") Long userId,
        @Param("startDate") String startDate,
        @Param("endDate") String endDate
    );
}