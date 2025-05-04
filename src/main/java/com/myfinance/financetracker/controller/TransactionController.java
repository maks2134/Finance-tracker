package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction Controller", description = "API для управления транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить транзакцию по ID", description = "Возвращает транзакцию по указанному ID")
    @ApiResponse(responseCode = "200", description = "Транзакция найдена")
    @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable final Long id) {
        Transaction transaction = transactionService.getTransactionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    @Operation(summary = "Получить транзакции по диапазону дат", description = "Возвращает список транзакций в указанном диапазоне дат")
    @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры дат")
    public ResponseEntity<List<Transaction>> getTransactionsByDateRange(
        @Parameter(description = "Начальная дата (формат yyyy-MM-dd)", required = false) @RequestParam(required = false) final String startDate,
        @Parameter(description = "Конечная дата (формат yyyy-MM-dd)", required = false) @RequestParam(required = false) final String endDate) {
        List<Transaction> transactions = transactionService.getTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    @Operation(summary = "Создать транзакцию", description = "Создает новую транзакцию")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        Transaction createdTransaction = transactionService.createOrUpdateTransaction(transaction);
        return ResponseEntity.ok(createdTransaction);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Массовое создание/обновление транзакций",
        description = "Создает или обновляет список транзакций за одну операцию")
    @ApiResponse(responseCode = "200", description = "Транзакции успешно обработаны")
    @ApiResponse(responseCode = "400", description = "Некорректные данные в запросе")
    public ResponseEntity<List<Transaction>> processTransactionsBulk(
        @RequestBody List<@Valid Transaction> transactions) {
        List<Transaction> processedTransactions = transactions.stream()
            .map(transactionService::createOrUpdateTransaction)
            .toList();

        return ResponseEntity.ok(processedTransactions);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить транзакцию", description = "Обновляет существующую транзакцию по ID")
    @ApiResponse(responseCode = "200", description = "Транзакция успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @Valid @RequestBody Transaction transactionDetails) {
        Transaction transaction = transactionService.getTransactionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id " + id));
        transaction.setAmount(transactionDetails.getAmount());
        transaction.setDate(transactionDetails.getDate());
        transaction.setDescription(transactionDetails.getDescription());
        Transaction updatedTransaction = transactionService.createOrUpdateTransaction(transaction);
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить транзакцию", description = "Удаляет транзакцию по ID")
    @ApiResponse(responseCode = "204", description = "Транзакция успешно удалена")
    @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-user-and-date")
    @Operation(summary = "Получить транзакции по пользователю и диапазону дат", description = "Возвращает список транзакций для указанного пользователя в указанном диапазоне дат")
    @ApiResponse(responseCode = "200", description = "Список транзакций успешно получен")
    @ApiResponse(responseCode = "400", description = "Некорректные параметры")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<List<Transaction>> getTransactionsByUserAndDateRange(
        @Parameter(description = "ID пользователя", required = true) @RequestParam Long userId,
        @Parameter(description = "Начальная дата (формат yyyy-MM-dd)", required = true) @RequestParam String startDate,
        @Parameter(description = "Конечная дата (формат yyyy-MM-dd)", required = true) @RequestParam String endDate) {
        List<Transaction> transactions = transactionService.getTransactionsByUserAndDateRange(userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
}