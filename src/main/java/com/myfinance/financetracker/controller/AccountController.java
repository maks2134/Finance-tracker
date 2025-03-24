package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.exception.ValidationException;
import com.myfinance.financetracker.model.Account;
import com.myfinance.financetracker.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Account Controller", description = "API для управления счетами")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить счет по ID", description = "Возвращает счет по указанному ID")
    @ApiResponse(responseCode = "200", description = "Счет найден")
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
        return ResponseEntity.ok(account);
    }

    @GetMapping
    @Operation(summary = "Получить все счета", description = "Возвращает список всех счетов")
    @ApiResponse(responseCode = "200", description = "Список счетов успешно получен")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    @Operation(summary = "Создать счет", description = "Создает новый счет")
    @ApiResponse(responseCode = "200", description = "Счет успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        if (account.getUser() == null || account.getUser().getId() == null) {
            throw new ValidationException("User ID is required");
        }
        Account createdAccount = accountService.createOrUpdateAccount(account);
        return ResponseEntity.ok(createdAccount);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить счет", description = "Обновляет существующий счет по ID")
    @ApiResponse(responseCode = "200", description = "Счет успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody Account accountDetails) {
        Account account = accountService.getAccountById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
        account.setName(accountDetails.getName());
        account.setType(accountDetails.getType());
        account.setBalance(accountDetails.getBalance());
        if (accountDetails.getUser() != null && accountDetails.getUser().getId() != null) {
            account.setUser(accountDetails.getUser());
        }
        Account updatedAccount = accountService.createOrUpdateAccount(account);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить счет", description = "Удаляет счет по ID")
    @ApiResponse(responseCode = "204", description = "Счет успешно удален")
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}