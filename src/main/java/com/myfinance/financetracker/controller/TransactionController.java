package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.service.TransactionService;
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


/**
 * REST-контроллер для управления транзакциями.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable final Long id) {
        Transaction transaction = transactionService.getTransactionById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Transaction not found with id " + id));
        return ResponseEntity.ok(transaction);
    }

    /**
     * GET эндпоинт для получения списка транзакций по диапазону дат.
     * Параметры запроса startDate и endDate являются необязательными.
     *
     * @param startDate начало диапазона (формат yyyy-MM-dd)
     * @param endDate конец диапазона (формат yyyy-MM-dd)
     * @return список транзакций в виде JSON
     */
    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
        @RequestParam(required = false) final String startDate,
        @RequestParam(required = false) final String endDate) {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate,
            endDate);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createOrUpdateTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Transaction>
        updateTransaction(@PathVariable Long id,
         @RequestBody Transaction transactionDetails) {
        Transaction transaction = transactionService.getTransactionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id "
                + id));
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setDate(transactionDetails.getDate());
        transaction.setDescription(transactionDetails.getDescription());
        Transaction updatedTransaction = transactionService.createOrUpdateTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user-and-date")
    public ResponseEntity<List<Transaction>> getTransactionsByUserAndDateRange(
        @RequestParam Long userId,
        @RequestParam String startDate,
        @RequestParam String endDate) {
        List<Transaction> transactions = transactionService.getTransactionsByUserAndDateRange(
            userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}