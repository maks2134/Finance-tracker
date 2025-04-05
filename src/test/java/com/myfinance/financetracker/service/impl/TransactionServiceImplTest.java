package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private Transaction transaction1;
    private Transaction transaction2;
    private Budget budget;

    @BeforeEach
    void setUp() {
        budget = new Budget();
        budget.setId(1L);
        budget.setSpent(100.0);
        budget.setAmount(1000.0);

        transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setAmount(50.0);
        transaction1.setDate("2023-01-01");
        transaction1.setBudget(budget);

        transaction2 = new Transaction();
        transaction2.setId(2L);
        transaction2.setAmount(75.0);
        transaction2.setDate("2023-01-15");
    }

    @Test
    void getTransactionById_ExistingId_ReturnsTransaction() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction1));

        Optional<Transaction> result = transactionService.getTransactionById(1L);

        assertTrue(result.isPresent());
        assertEquals(transaction1, result.get());
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void getTransactionById_NonExistingId_ReturnsEmpty() {
        when(transactionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Transaction> result = transactionService.getTransactionById(99L);

        assertFalse(result.isPresent());
        verify(transactionRepository, times(1)).findById(99L);
    }

    @Test
    void getTransactionsByDateRange_WithBothDates_ReturnsFilteredTransactions() {
        List<Transaction> allTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAll()).thenReturn(allTransactions);

        List<Transaction> result = transactionService.getTransactionsByDateRange("2023-01-01", "2023-01-10");

        assertEquals(1, result.size());
        assertEquals(transaction1, result.get(0));
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void getTransactionsByDateRange_WithStartDateOnly_ReturnsFilteredTransactions() {
        List<Transaction> allTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAll()).thenReturn(allTransactions);

        List<Transaction> result = transactionService.getTransactionsByDateRange("2023-01-10", null);

        assertEquals(1, result.size());
        assertEquals(transaction2, result.get(0));
    }

    @Test
    void getTransactionsByDateRange_WithEndDateOnly_ReturnsFilteredTransactions() {
        List<Transaction> allTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAll()).thenReturn(allTransactions);

        List<Transaction> result = transactionService.getTransactionsByDateRange(null, "2023-01-10");

        assertEquals(1, result.size());
        assertEquals(transaction1, result.get(0));
    }

    @Test
    void getTransactionsByDateRange_WithNoDates_ReturnsAllTransactions() {
        List<Transaction> allTransactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findAll()).thenReturn(allTransactions);

        List<Transaction> result = transactionService.getTransactionsByDateRange(null, null);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(allTransactions));
    }

    @Test
    void createOrUpdateTransaction_NewTransactionWithBudget_UpdatesBudget() {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(100.0);
        newTransaction.setBudget(budget);

        when(transactionRepository.save(newTransaction)).thenReturn(newTransaction);
        when(budgetRepository.save(budget)).thenReturn(budget);

        Transaction result = transactionService.createOrUpdateTransaction(newTransaction);

        assertEquals(newTransaction, result);
        assertEquals(200.0, budget.getSpent()); // 100 (initial) + 100 (new)
        verify(transactionRepository, times(1)).save(newTransaction);
        verify(budgetRepository, times(1)).save(budget);
    }

    @Test
    void createOrUpdateTransaction_ExistingTransaction_DoesNotUpdateBudget() {
        transaction1.setAmount(60.0); // Changing amount of existing transaction
        when(transactionRepository.save(transaction1)).thenReturn(transaction1);

        Transaction result = transactionService.createOrUpdateTransaction(transaction1);

        assertEquals(transaction1, result);
        assertEquals(100.0, budget.getSpent()); // Should remain unchanged
        verify(transactionRepository, times(1)).save(transaction1);
        verify(budgetRepository, never()).save(any());
    }

    @Test
    void createOrUpdateTransaction_NewTransactionWithoutBudget_DoesNotUpdateBudget() {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(100.0);

        when(transactionRepository.save(newTransaction)).thenReturn(newTransaction);

        Transaction result = transactionService.createOrUpdateTransaction(newTransaction);

        assertEquals(newTransaction, result);
        verify(transactionRepository, times(1)).save(newTransaction);
        verify(budgetRepository, never()).save(any());
    }

    @Test
    void deleteTransaction_ValidId_DeletesTransaction() {
        doNothing().when(transactionRepository).deleteById(1L);

        transactionService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTransactionsByUserAndDateRange_CallsRepositoryMethod() {
        List<Transaction> expected = Arrays.asList(transaction1);
        when(transactionRepository.findTransactionsByUserAndDateRange(1L, "2023-01-01", "2023-01-31"))
            .thenReturn(expected);

        List<Transaction> result = transactionService.getTransactionsByUserAndDateRange(1L, "2023-01-01", "2023-01-31");

        assertEquals(expected, result);
        verify(transactionRepository, times(1))
            .findTransactionsByUserAndDateRange(1L, "2023-01-01", "2023-01-31");
    }
}