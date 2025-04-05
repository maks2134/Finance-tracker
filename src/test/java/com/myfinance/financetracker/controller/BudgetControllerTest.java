package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.service.BudgetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BudgetControllerTest {

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private BudgetController budgetController;

    @Test
    void getBudgetById_ShouldReturnBudget() {
        Budget budget = new Budget();
        budget.setId(1L);
        when(budgetService.getBudgetById(1L)).thenReturn(Optional.of(budget));

        ResponseEntity<Budget> response = budgetController.getBudgetById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getBudgetById_ShouldThrowNotFound() {
        when(budgetService.getBudgetById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            budgetController.getBudgetById(1L);
        });
    }

    @Test
    void getAllBudgets_ShouldReturnAllBudgets() {
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());
        when(budgetService.getAllBudgets()).thenReturn(budgets);

        ResponseEntity<List<Budget>> response = budgetController.getAllBudgets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createBudget_ShouldReturnCreatedBudget() {
        Budget budget = new Budget();
        when(budgetService.createOrUpdateBudget(budget)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.createBudget(budget);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void createBudgetWithCategories_ShouldReturnCreatedBudget() {
        Budget budget = new Budget();
        List<Long> categoryIds = Arrays.asList(1L, 2L);
        when(budgetService.createOrUpdateBudgetWithCategoryIds(budget, categoryIds)).thenReturn(budget);

        ResponseEntity<Budget> response = budgetController.createBudgetWithCategories(budget, categoryIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void updateBudget_ShouldReturnUpdatedBudget() {
        Budget existingBudget = new Budget();
        existingBudget.setId(1L);
        Budget updatedDetails = new Budget();
        updatedDetails.setName("Updated Budget");

        when(budgetService.getBudgetById(1L)).thenReturn(Optional.of(existingBudget));
        when(budgetService.createOrUpdateBudget(existingBudget)).thenReturn(existingBudget);

        ResponseEntity<Budget> response = budgetController.updateBudget(1L, updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Budget", response.getBody().getName());
    }

    @Test
    void deleteBudget_ShouldReturnNoContent() {
        doNothing().when(budgetService).deleteBudget(1L);

        ResponseEntity<Void> response = budgetController.deleteBudget(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void getBudgetsByLimit_ShouldReturnFilteredBudgets() {
        List<Budget> budgets = Arrays.asList(new Budget(), new Budget());
        when(budgetService.getBudgetsByLimitLessThanOrEqual(1000.0)).thenReturn(budgets);

        ResponseEntity<List<Budget>> response =
            budgetController.getBudgetsByLimitLessThanOrEqual(1000.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }
}