package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  /**
   * GET эндпоинт для получения транзакции по идентификатору.
   *
   * @param id идентификатор транзакции
   * @return транзакция в виде JSON
   */
  @GetMapping("/{id}")
  public ResponseEntity<Transaction> getTransactionById(@PathVariable final Long id) {
    Transaction transaction = transactionService.getTransactionById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
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
    List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
    return ResponseEntity.ok(transactions);
  }
}