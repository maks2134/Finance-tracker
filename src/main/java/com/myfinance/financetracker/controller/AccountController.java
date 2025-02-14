package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Account;
import com.myfinance.financetracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST-контроллер для управления счетами.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  private final AccountService accountService;

  @Autowired
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
    Account account = accountService.getAccountById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    return ResponseEntity.ok(account);
  }

  @GetMapping
  public ResponseEntity<List<Account>> getAllAccounts() {
    List<Account> accounts = accountService.getAllAccounts();
    return ResponseEntity.ok(accounts);
  }

  @PostMapping
  public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    Account createdAccount = accountService.createOrUpdateAccount(account);
    return ResponseEntity.ok(createdAccount);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account accountDetails) {
    Account account = accountService.getAccountById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
    account.setName(accountDetails.getName());
    account.setType(accountDetails.getType());
    account.setBalance(accountDetails.getBalance());
    Account updatedAccount = accountService.createOrUpdateAccount(account);
    return ResponseEntity.ok(updatedAccount);
  }
}