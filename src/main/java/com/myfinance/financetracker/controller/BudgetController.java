package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Budget Controller", description = "API для управления бюджетами")
public class BudgetController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить бюджет по ID", description = "Возвращает бюджет по указанному ID")
    @ApiResponse(responseCode = "200", description = "Бюджет найден")
    @ApiResponse(responseCode = "404", description = "Бюджет не найден")
    public ResponseEntity<Budget> getBudgetById(@PathVariable Long id) {
        Budget budget = budgetService.getBudgetById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id " + id));
        return ResponseEntity.ok(budget);
    }

    @GetMapping
    @Operation(summary = "Получить все бюджеты", description = "Возвращает список всех бюджетов")
    @ApiResponse(responseCode = "200", description = "Список бюджетов успешно получен")
    public ResponseEntity<List<Budget>> getAllBudgets() {
        List<Budget> budgets = budgetService.getAllBudgets();
        return ResponseEntity.ok(budgets);
    }

    @PostMapping("/with-categories")
    @Operation(summary = "Создать бюджет с категориями", description = "Создает новый бюджет с указанными категориями")
    @ApiResponse(responseCode = "200", description = "Бюджет найден")
    @ApiResponse(responseCode = "404", description = "Бюджет не найден")
    public ResponseEntity<Budget> createBudgetWithCategories(
        @RequestBody Budget budget,
        @Parameter(description = "Список ID категорий", required = true) @RequestParam List<Long> categoryIds) {
        Budget createdBudget = budgetService.createOrUpdateBudgetWithCategoryIds(budget, categoryIds);
        return ResponseEntity.ok(createdBudget);
    }

    @PostMapping
    @Operation(summary = "Создать бюджет", description = "Создает новый бюджет")
    @ApiResponse(responseCode = "200", description = "Бюджет найден")
    @ApiResponse(responseCode = "404", description = "Бюджет не найден")
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody Budget budget) {
        Budget createdBudget = budgetService.createOrUpdateBudget(budget);
        return ResponseEntity.ok(createdBudget);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить бюджет", description = "Обновляет существующий бюджет по ID")
    @ApiResponse(responseCode = "200", description = "Бюджет найден")
    @ApiResponse(responseCode = "404", description = "Бюджет не найден")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @Valid @RequestBody Budget budgetDetails) {
        Budget budget = budgetService.getBudgetById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id " + id));
        budget.setName(budgetDetails.getName());
        budget.setLimitAmount(budgetDetails.getLimitAmount());
        Budget updatedBudget = budgetService.createOrUpdateBudget(budget);
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить бюджет", description = "Удаляет бюджет по ID")
    @ApiResponse(responseCode = "200", description = "Бюджет найден")
    @ApiResponse(responseCode = "404", description = "Бюджет не найден")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-limit")
    @Operation(summary = "Получить бюджеты по лимиту", description = "Возвращает бюджеты с лимитом меньше или равным указанному")
    @ApiResponse(responseCode = "200", description = "Список бюджетов успешно получен")
    public ResponseEntity<List<Budget>> getBudgetsByLimitLessThanOrEqual(
        @Parameter(description = "Лимит бюджета", required = true) @RequestParam Double limit) {
        List<Budget> budgets = budgetService.getBudgetsByLimitLessThanOrEqual(limit);
        return ResponseEntity.ok(budgets);
    }
}