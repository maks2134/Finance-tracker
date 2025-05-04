// src/main/java/com/myfinance/financetracker/service/BudgetService.java
package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Budget;
import java.util.List;
import java.util.Optional;

public interface BudgetService {

    Optional<Budget> getBudgetById(Long id);

    List<Budget> getAllBudgets();

    // --- ИЗМЕНЕНИЕ: Добавляем параметр categoryIds ---
    /**
     * Сохранение (или обновление) бюджета.
     * При обновлении можно передать новый список ID категорий.
     *
     * @param budget Бюджет для сохранения/обновления (может содержать ID для обновления)
     * @param categoryIds Новый список ID категорий для связи (может быть null, если не обновлять категории)
     * @return Сохраненный или обновленный бюджет
     */
    Budget createOrUpdateBudget(Budget budget, List<Long> categoryIds);

    // --- ОСТАВЛЯЕМ СТАРЫЙ МЕТОД (для совместимости или удаления позже) ---
    @Deprecated // Помечаем как устаревший, если новый метод его заменяет
    Budget createOrUpdateBudget(Budget budget);


    Budget createOrUpdateBudgetWithCategoryIds(Budget budget, List<Long> categoryIds);

    void deleteBudget(Long id);

    List<Budget> getBudgetsByLimitLessThanOrEqual(Double limit);
}