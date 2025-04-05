package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.repository.CategoryRepository;
import com.myfinance.financetracker.service.BudgetService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository; // Добавляем репозиторий категорий

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository; // Инициализируем репозиторий категорий
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
        // Присоединяем категории к текущей сессии
        if (budget.getCategories() != null) {
            List<Long> listCategoriesId = budget.getCategories().stream()
                .map(Category::getId).toList();
            List<Category> managedCategories = categoryRepository.findAllById(listCategoriesId);
            budget.setCategories(managedCategories);
        }
        return budgetRepository.save(budget);
    }

    @Override
    public Budget createOrUpdateBudgetWithCategoryIds(Budget budget, List<Long> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(categoryIds);
            budget.setCategories(categories);
        } else {
            budget.setCategories(Collections.emptyList());
        }
        return budgetRepository.save(budget);
    }

    @Override
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }

    @Override
    public List<Budget> getBudgetsByLimitLessThanOrEqual(Double limit) {
        return budgetRepository.findBudgetsByLimitLessThanOrEqual(limit);
    }
}