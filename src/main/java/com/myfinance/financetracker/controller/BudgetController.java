package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.service.BudgetService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private final BudgetService budgetService;

	@Autowired
	public BudgetController(BudgetService budgetService) {
		this.budgetService = budgetService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Budget> getBudgetById(@PathVariable Long id) {
		Budget budget =
			budgetService
				.getBudgetById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Budget not found with id " + id));
		return ResponseEntity.ok(budget);
	}

	@GetMapping
	public ResponseEntity<List<Budget>> getAllBudgets() {
		List<Budget> budgets = budgetService.getAllBudgets();
		return ResponseEntity.ok(budgets);
	}

	@PostMapping
	public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
		Budget createdBudget = budgetService.createOrUpdateBudget(budget);
		return ResponseEntity.ok(createdBudget);
	}

	@PostMapping("/with-categories")
	public ResponseEntity<Budget> createBudgetWithCategories(
		@RequestBody Budget budget,
		@RequestParam List<Long> categoryIds) {
		Budget createdBudget = budgetService.createOrUpdateBudgetWithCategoryIds(budget, categoryIds);
		return ResponseEntity.ok(createdBudget);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Budget> updateBudget(
		@PathVariable Long id, @RequestBody Budget budgetDetails) {
		Budget budget =
			budgetService
				.getBudgetById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Budget not found with id " + id));
		budget.setName(budgetDetails.getName());
		budget.setLimitAmount(budgetDetails.getLimitAmount());
		Budget updatedBudget = budgetService.createOrUpdateBudget(budget);
		return ResponseEntity.ok(updatedBudget);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
		budgetService.deleteBudget(id);
		return ResponseEntity.noContent().build();
	}
}