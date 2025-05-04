// src/main/java/com/myfinance/financetracker/service/impl/BudgetServiceImpl.java
package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.repository.CategoryRepository;
import com.myfinance.financetracker.service.BudgetService;
import jakarta.transaction.Transactional; // Добавляем Transactional
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.myfinance.financetracker.exception.ResourceNotFoundException; // Добавляем

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Budget> getBudgetById(Long id) {
        // Используем fetch для загрузки категорий сразу (если нужно)
        // return budgetRepository.findById(id); // Старый вариант
         return budgetRepository.findById(id).map(budget -> {
             // Инициализация ленивой коллекции категорий, если они нужны в ответе
             // Hibernate.initialize(budget.getCategories()); // <-- Раскомментируй, если используешь Hibernate и хочешь вернуть категории в GET /api/budgets/{id}
             return budget;
         });
    }

    @Override
    public List<Budget> getAllBudgets() {
        // return budgetRepository.findAll(); // Старый вариант
         // Загружаем бюджеты и инициализируем их категории, чтобы избежать N+1 запросов при доступе к категориям позже
         List<Budget> budgets = budgetRepository.findAll();
         // budgets.forEach(budget -> Hibernate.initialize(budget.getCategories())); // <-- Раскомментируй, если используешь Hibernate и хочешь вернуть категории в GET /api/budgets
         return budgets;
    }

    // --- НОВАЯ РЕАЛИЗАЦИЯ createOrUpdateBudget с categoryIds ---
    @Override
    @Transactional // Важно для корректной работы с коллекциями
    public Budget createOrUpdateBudget(Budget budgetDetails, List<Long> categoryIds) {
        Budget budgetToSave;
        if (budgetDetails.getId() != null) {
            // --- Обновление ---
            budgetToSave = budgetRepository.findById(budgetDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id " + budgetDetails.getId()));
            // Обновляем поля из budgetDetails
            budgetToSave.setName(budgetDetails.getName());
            budgetToSave.setLimitAmount(budgetDetails.getLimitAmount());
            // Поле spent НЕ обновляем здесь, оно обновляется при добавлении транзакций
        } else {
            // --- Создание ---
            budgetToSave = budgetDetails; // Используем переданный объект
            // Устанавливаем spent в 0 для нового бюджета, если не был установлен
            if (budgetToSave.getSpent() == null) {
                budgetToSave.setSpent(0.0);
            }
        }

        // --- Обновление категорий ---
        if (categoryIds != null) { // Обновляем категории, только если categoryIds передан (не null)
            if (!categoryIds.isEmpty()) {
                // Находим существующие категории по ID
                List<Category> categories = categoryRepository.findAllById(categoryIds);
                // Проверяем, все ли ID найдены (опционально, но полезно)
                if(categories.size() != categoryIds.size()){
                     // Можно выбросить исключение или просто проигнорировать ненайденные ID
                     System.err.println("Warning: Some category IDs not found: " + categoryIds);
                }
                 // Устанавливаем найденные категории на бюджет
                 // JPA/Hibernate автоматически обновит связующую таблицу budget_category
                budgetToSave.setCategories(categories);
            } else {
                // Если передан пустой список ID, очищаем категории бюджета
                budgetToSave.setCategories(Collections.emptyList());
            }
        }
        // Если categoryIds == null, список категорий у budgetToSave НЕ изменяется

        return budgetRepository.save(budgetToSave);
    }

    // --- Старый метод createOrUpdateBudget (можно удалить или оставить как @Deprecated) ---
    @Override
    @Deprecated
    public Budget createOrUpdateBudget(Budget budget) {
        // Вызываем новый метод, передавая null для categoryIds, чтобы категории не обновлялись
        return createOrUpdateBudget(budget, null);
    }

    @Override
    @Transactional
    public Budget createOrUpdateBudgetWithCategoryIds(Budget budget, List<Long> categoryIds) {
        // Этот метод теперь просто вызывает основной метод createOrUpdateBudget
        return createOrUpdateBudget(budget, categoryIds);
    }

    @Override
    @Transactional
    public void deleteBudget(Long id) {
        // Можно добавить логику отвязывания категорий перед удалением, если нужно
        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);
        } else {
             throw new ResourceNotFoundException("Budget not found with id " + id);
        }
    }

    @Override
    public List<Budget> getBudgetsByLimitLessThanOrEqual(Double limit) {
        return budgetRepository.findBudgetsByLimitLessThanOrEqual(limit);
    }
}