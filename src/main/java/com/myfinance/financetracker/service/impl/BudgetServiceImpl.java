package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.dao.BudgetDao;
import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.service.BudgetService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {

	private final BudgetDao budgetDao;

	@Autowired
	public BudgetServiceImpl(BudgetDao budgetDao) {
		this.budgetDao = budgetDao;
	}

	@Override
	public Optional<Budget> getBudgetById(Long id) {
		return budgetDao.findById(id);
	}

	@Override
	public List<Budget> getAllBudgets() {
		return budgetDao.findAll();
	}

	@Override
	public Budget createOrUpdateBudget(Budget budget) {
		return budgetDao.save(budget);
	}

	@Override
	public void deleteBudget(Long id) {
		budgetDao.deleteById(id);
	}
}