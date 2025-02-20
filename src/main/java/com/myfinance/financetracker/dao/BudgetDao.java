package com.myfinance.financetracker.dao;

import com.myfinance.financetracker.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetDao {
	Optional<Budget> findById(Long id);

	List<Budget> findAll();

	Budget save(Budget budget);

	void deleteById(Long id);
}