package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск пользователей по amount транзакции
    @Query("SELECT DISTINCT u FROM User u JOIN u.transactions t WHERE t.amount = :amount")
    List<User> findUsersByTransactionAmount(@Param("amount") Double amount);
}