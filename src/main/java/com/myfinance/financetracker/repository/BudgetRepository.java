package com.myfinance.financetracker.repository;

import com.myfinance.financetracker.model.Budget;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query(value = "SELECT * FROM budgets WHERE limit_amount <= :limit", nativeQuery = true)
    List<Budget> findBudgetsByLimitLessThanOrEqual(@Param("limit") Double limit);
}