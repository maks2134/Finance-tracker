package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.service.BudgetService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

	private final BudgetRepository budgetRepository;

	@Autowired
	public BudgetServiceImpl(BudgetRepository budgetRepository) {
		this.budgetRepository = budgetRepository;
	}

	@Override
	public Optional<Budget> getBudgetById(Long id) {
		return budgetRepository.findById(id);
	}

	@Override
	public List<Budget> getAllBudgets() {
		return budgetRepository.findAll();
	}

	@Override
	public Budget createOrUpdateBudget(Budget budget) {
		return budgetRepository.save(budget);
	}

	@Override
	public void deleteBudget(Long id) {
		budgetRepository.deleteById(id);
	}
}