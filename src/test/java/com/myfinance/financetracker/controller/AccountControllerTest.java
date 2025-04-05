package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.model.Account;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.AccountService;
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
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Test
    void getAccountById_ShouldReturnAccount() {
        Account account = new Account();
        account.setId(1L);
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));

        ResponseEntity<Account> response = accountController.getAccountById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getAllAccounts_ShouldReturnAllAccounts() {
        List<Account> accounts = Arrays.asList(new Account(), new Account());
        when(accountService.getAllAccounts()).thenReturn(accounts);

        ResponseEntity<List<Account>> response = accountController.getAllAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() {
        Account account = new Account();
        User user = new User();
        user.setId(1L);
        account.setUser(user);
        when(accountService.createOrUpdateAccount(account)).thenReturn(account);

        ResponseEntity<Account> response = accountController.createAccount(account);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateAccount_ShouldReturnUpdatedAccount() {
        Account existingAccount = new Account();
        existingAccount.setId(1L);
        Account updatedDetails = new Account();
        updatedDetails.setName("Updated");

        when(accountService.getAccountById(1L)).thenReturn(Optional.of(existingAccount));
        when(accountService.createOrUpdateAccount(any())).thenReturn(existingAccount);

        ResponseEntity<Account> response = accountController.updateAccount(1L, updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated", response.getBody().getName());
    }

    @Test
    void deleteAccount_ShouldReturnNoContent() {
        doNothing().when(accountService).deleteAccount(1L);

        ResponseEntity<Void> response = accountController.deleteAccount(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}