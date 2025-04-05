package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Category;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetServiceImplTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BudgetServiceImpl budgetService;

    private Budget budget1;
    private Budget budget2;
    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        budget1 = new Budget("Groceries", 500.0);
        budget1.setId(1L);
        budget1.setSpent(200.0);

        budget2 = new Budget("Entertainment", 300.0);
        budget2.setId(2L);
        budget2.setSpent(150.0);

        category1 = new Category();
        category1.setId(1L);
        category1.setName("Food");

        category2 = new Category();
        category2.setId(2L);
        category2.setName("Leisure");
    }

    @Test
    void getBudgetById_ExistingId_ReturnsBudget() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget1));

        Optional<Budget> result = budgetService.getBudgetById(1L);

        assertTrue(result.isPresent());
        assertEquals(budget1, result.get());
        assertEquals("Groceries", result.get().getName());
        verify(budgetRepository, times(1)).findById(1L);
    }

    @Test
    void getBudgetById_NonExistingId_ReturnsEmpty() {
        when(budgetRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Budget> result = budgetService.getBudgetById(99L);

        assertFalse(result.isPresent());
        verify(budgetRepository, times(1)).findById(99L);
    }

    @Test
    void getAllBudgets_ReturnsAllBudgets() {
        List<Budget> allBudgets = Arrays.asList(budget1, budget2);
        when(budgetRepository.findAll()).thenReturn(allBudgets);

        List<Budget> result = budgetService.getAllBudgets();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(allBudgets));
        verify(budgetRepository, times(1)).findAll();
    }

    @Test
    void createOrUpdateBudget_NewBudget_ReturnsSavedBudget() {
        Budget newBudget = new Budget("Utilities", 400.0);

        when(budgetRepository.save(newBudget)).thenReturn(newBudget);

        Budget result = budgetService.createOrUpdateBudget(newBudget);

        assertEquals(newBudget, result);
        verify(budgetRepository, times(1)).save(newBudget);
    }

    @Test
    void createOrUpdateBudget_WithCategories_ReturnsSavedBudgetWithManagedCategories() {
        Budget budgetWithCategories = new Budget("Shopping", 600.0);
        budgetWithCategories.setCategories(Arrays.asList(category1, category2));

        when(categoryRepository.findAllById(anyList())).thenReturn(Arrays.asList(category1, category2));
        when(budgetRepository.save(budgetWithCategories)).thenReturn(budgetWithCategories);

        Budget result = budgetService.createOrUpdateBudget(budgetWithCategories);

        assertEquals(budgetWithCategories, result);
        assertEquals(2, result.getCategories().size());
        verify(categoryRepository, times(1)).findAllById(anyList());
        verify(budgetRepository, times(1)).save(budgetWithCategories);
    }

    @Test
    void createOrUpdateBudgetWithCategoryIds_ValidIds_ReturnsBudgetWithCategories() {
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        Budget budget = new Budget("Travel", 1000.0);

        when(categoryRepository.findAllById(categoryIds)).thenReturn(Arrays.asList(category1, category2));
        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.createOrUpdateBudgetWithCategoryIds(budget, categoryIds);

        assertEquals(budget, result);
        assertEquals(2, result.getCategories().size());
        verify(categoryRepository, times(1)).findAllById(categoryIds);
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void createOrUpdateBudgetWithCategoryIds_EmptyIds_ReturnsBudgetWithoutCategories() {
        Budget budget = new Budget("Education", 800.0);

        when(budgetRepository.save(budget)).thenReturn(budget);

        Budget result = budgetService.createOrUpdateBudgetWithCategoryIds(budget, Collections.emptyList());

        assertEquals(budget, result);
        assertTrue(result.getCategories().isEmpty());
        verify(categoryRepository, never()).findAllById(anyList());
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void deleteBudget_ValidId_DeletesBudget() {
        doNothing().when(budgetRepository).deleteById(1L);

        budgetService.deleteBudget(1L);

        verify(budgetRepository, times(1)).deleteById(1L);
    }

    @Test
    void getBudgetsByLimitLessThanOrEqual_ReturnsFilteredBudgets() {
        List<Budget> expectedBudgets = Arrays.asList(budget2);
        when(budgetRepository.findBudgetsByLimitLessThanOrEqual(400.0)).thenReturn(expectedBudgets);

        List<Budget> result = budgetService.getBudgetsByLimitLessThanOrEqual(400.0);

        assertEquals(1, result.size());
        assertEquals(budget2, result.get(0));
        verify(budgetRepository, times(1)).findBudgetsByLimitLessThanOrEqual(400.0);
    }

    @Test
    void getRemaining_CalculatesCorrectValue() {
        when(budgetRepository.findById(1L)).thenReturn(Optional.of(budget1));

        Optional<Budget> result = budgetService.getBudgetById(1L);

        assertTrue(result.isPresent());
        assertEquals(300.0, result.get().getRemaining()); // 500 limit - 200 spent
    }

    @Test
    void createOrUpdateBudget_ZeroLimit_SavesCorrectly() {
        Budget zeroLimitBudget = new Budget("Zero Budget", 0.0);

        when(budgetRepository.save(zeroLimitBudget)).thenReturn(zeroLimitBudget);

        Budget result = budgetService.createOrUpdateBudget(zeroLimitBudget);

        assertEquals(zeroLimitBudget, result);
        assertEquals(0.0, result.getLimitAmount());
        verify(budgetRepository, times(1)).save(zeroLimitBudget);
    }
}