package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.service.TransactionService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    void getTransactionById_ShouldReturnTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        when(transactionService.getTransactionById(1L)).thenReturn(Optional.of(transaction));

        ResponseEntity<Transaction> response = transactionController.getTransactionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getTransactionsByDateRange_ShouldReturnFilteredTransactions() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByDateRange("2023-01-01", "2023-01-31")).thenReturn(transactions);

        ResponseEntity<List<Transaction>> response =
            transactionController.getTransactionsByDateRange("2023-01-01", "2023-01-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createTransaction_ShouldReturnCreatedTransaction() {
        Transaction transaction = new Transaction();
        when(transactionService.createOrUpdateTransaction(transaction)).thenReturn(transaction);

        ResponseEntity<Transaction> response = transactionController.createTransaction(transaction);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void processTransactionsBulk_ShouldReturnProcessedTransactions() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.createOrUpdateTransaction(any())).thenReturn(new Transaction());

        ResponseEntity<List<Transaction>> response =
            transactionController.processTransactionsBulk(transactions);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void updateTransaction_ShouldReturnUpdatedTransaction() {
        Transaction existing = new Transaction();
        existing.setId(1L);
        Transaction updates = new Transaction();
        updates.setAmount(100.0);

        when(transactionService.getTransactionById(1L)).thenReturn(Optional.of(existing));
        when(transactionService.createOrUpdateTransaction(existing)).thenReturn(existing);

        ResponseEntity<Transaction> response = transactionController.updateTransaction(1L, updates);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody().getAmount());
    }

    @Test
    void getTransactionsByUserAndDateRange_ShouldReturnFilteredTransactions() {
        List<Transaction> transactions = Arrays.asList(new Transaction(), new Transaction());
        when(transactionService.getTransactionsByUserAndDateRange(1L, "2023-01-01", "2023-01-31"))
            .thenReturn(transactions);

        ResponseEntity<List<Transaction>> response =
            transactionController.getTransactionsByUserAndDateRange(1L, "2023-01-01", "2023-01-31");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void deleteTransaction_ShouldReturnNoContent() {
        doNothing().when(transactionService).deleteTransaction(1L);

        ResponseEntity<Void> response = transactionController.deleteTransaction(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}