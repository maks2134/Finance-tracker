package com.myfinance.financetracker.dao.impl;

import com.myfinance.financetracker.dao.BudgetDao;
import com.myfinance.financetracker.model.Budget;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class BudgetDaoImpl implements BudgetDao {

	private final ConcurrentHashMap<Long, Budget> budgetStore = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1);

	@Override
	public Optional<Budget> findById(Long id) {
		return Optional.ofNullable(budgetStore.get(id));
	}

	@Override
	public List<Budget> findAll() {
		return new ArrayList<>(budgetStore.values());
	}

	@Override
	public Budget save(Budget budget) {
		if (budget.getId() == null) {
			budget.setId(idGenerator.getAndIncrement());
		}
		budgetStore.put(budget.getId(), budget);
		return budget;
	}

	@Override
	public void deleteById(Long id) {
		budgetStore.remove(id);
	}
}