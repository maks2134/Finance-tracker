package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Убедись, что импорт правильный
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000") // Разрешаем CORS для фронтенда
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
    @ApiResponse(responseCode = "200", description = "Бюджет успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные") // Добавил 400
    @ApiResponse(responseCode = "404", description = "Категории не найдены") // Уточнил 404
    public ResponseEntity<Budget> createBudgetWithCategories(
        @Valid @RequestBody Budget budget, // Добавил @Valid
        @Parameter(description = "Список ID категорий", required = true)
        @RequestParam List<Long> categoryIds) {
        // Используем метод сервиса, который ожидает ID категорий
        // Убедись, что BudgetServiceImpl.createOrUpdateBudgetWithCategoryIds корректно реализован
        Budget createdBudget = budgetService.createOrUpdateBudgetWithCategoryIds(budget, categoryIds);
        return ResponseEntity.ok(createdBudget);
    }

    @PostMapping
    @Operation(summary = "Создать бюджет (без категорий)", description = "Создает новый бюджет без привязки категорий")
    @ApiResponse(responseCode = "200", description = "Бюджет успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные") // Добавил 400
    public ResponseEntity<Budget> createBudget(@Valid @RequestBody Budget budget) {
        // Вызовет старый или новый метод сервиса createOrUpdateBudget(budget, null)
        Budget createdBudget = budgetService.createOrUpdateBudget(budget);
        return ResponseEntity.ok(createdBudget);
    }

    // --- ОБНОВЛЕННЫЙ МЕТОД ---
    @PutMapping("/{id}")
    @Operation(summary = "Обновить бюджет", description = "Обновляет существующий бюджет по ID, включая категории")
    @ApiResponse(responseCode = "200", description = "Бюджет успешно обновлен")
    @ApiResponse(responseCode = "400", description = "Некорректные данные") // Добавил 400
    @ApiResponse(responseCode = "404", description = "Бюджет или категории не найдены") // Уточнил 404
    public ResponseEntity<Budget> updateBudget(
        @Parameter(description = "ID обновляемого бюджета", required = true) @PathVariable Long id,
        @Valid @RequestBody Budget budgetDetails, // Принимаем основные данные в теле
        @Parameter(description = "Новый список ID категорий для бюджета. Если параметр ПРИСУТСТВУЕТ (даже пустой), категории будут ПЕРЕЗАПИСАНЫ. Если ПАРАМЕТР ОТСУТСТВУЕТ, категории НЕ ИЗМЕНЯТСЯ.", required = false)
        @RequestParam(required = false) List<Long> categoryIds // Принимаем список ID как параметр
    ) {
        // Устанавливаем ID в объект budgetDetails, чтобы сервис понял, что это обновление
        budgetDetails.setId(id);
        // Вызываем ОБНОВЛЕННЫЙ метод сервиса createOrUpdateBudget(Budget budget, List<Long> categoryIds)
        // Если categoryIds == null, сервис не будет трогать категории.
        // Если categoryIds пустой список [], сервис очистит категории у бюджета.
        // Если categoryIds = [1, 2], сервис установит категории с ID 1 и 2.
        Budget updatedBudget = budgetService.createOrUpdateBudget(budgetDetails, categoryIds);
        return ResponseEntity.ok(updatedBudget);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить бюджет", description = "Удаляет бюджет по ID")
    @ApiResponse(responseCode = "204", description = "Бюджет успешно удален") // Исправил код на 204
    @ApiResponse(responseCode = "404", description = "Бюджет не найден")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build(); // Стандартный ответ для DELETE
    }

    // Оставляем как есть
    @GetMapping("/by-limit")
    @Operation(summary = "Получить бюджеты по лимиту", description = "Возвращает бюджеты с лимитом меньше или равным указанному")
    @ApiResponse(responseCode = "200", description = "Список бюджетов успешно получен")
    public ResponseEntity<List<Budget>> getBudgetsByLimitLessThanOrEqual(
        @Parameter(description = "Лимит бюджета", required = true) @RequestParam Double limit) {
        List<Budget> budgets = budgetService.getBudgetsByLimitLessThanOrEqual(limit);
        return ResponseEntity.ok(budgets);
    }
}