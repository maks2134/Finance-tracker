package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetService {
    Optional<Budget> getBudgetById(Long id);

    List<Budget> getAllBudgets();

    Budget createOrUpdateBudget(Budget budget);

    Budget createOrUpdateBudgetWithCategoryIds(Budget budget, List<Long> categoryIds);

    void deleteBudget(Long id);

    List<Budget> getBudgetsByLimitLessThanOrEqual(Double limit);
}