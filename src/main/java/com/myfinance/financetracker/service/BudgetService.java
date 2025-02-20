package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetService {
	Optional<Budget> getBudgetById(Long id);

	List<Budget> getAllBudgets();

	Budget createOrUpdateBudget(Budget budget);

	void deleteBudget(Long id);
}