package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Account;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.AccountRepository;
import com.myfinance.financetracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testAccount = new Account();
        testAccount.setId(1L);
        testAccount.setName("Test Account");
        testAccount.setUser(testUser);
    }

    @Test
    void getAccountById_WhenAccountExists_ShouldReturnAccount() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        Optional<Account> result = accountService.getAccountById(1L);

        assertTrue(result.isPresent(), "Account should be present");
        assertEquals(testAccount, result.get(), "Returned account should match test account");
        assertEquals("Test Account", result.get().getName(), "Account name should match");
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void getAccountById_WhenAccountNotExists_ShouldReturnEmpty() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Account> result = accountService.getAccountById(1L);

        assertFalse(result.isPresent(), "Account should not be present");
        verify(accountRepository, times(1)).findById(1L);
    }

    @Test
    void getAllAccounts_ShouldReturnListOfAccounts() {
        List<Account> accounts = Collections.singletonList(testAccount);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAllAccounts();

        assertFalse(result.isEmpty(), "Result list should not be empty");
        assertEquals(1, result.size(), "Result list should have one account");
        assertEquals(testAccount, result.get(0), "Returned account should match test account");
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void createOrUpdateAccount_WithValidUser_ShouldSaveAccount() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(accountRepository.save(testAccount)).thenReturn(testAccount);

        Account result = accountService.createOrUpdateAccount(testAccount);

        assertNotNull(result, "Saved account should not be null");
        assertEquals(testUser, result.getUser(), "User should match");
        assertEquals("testUser", result.getUser().getUsername(), "Username should match");
        verify(userRepository, times(1)).findById(1L);
        verify(accountRepository, times(1)).save(testAccount);
    }

    @Test
    void createOrUpdateAccount_WithNullUser_ShouldThrowException() {
        testAccount.setUser(null);

        Exception exception = assertThrows(ResourceNotFoundException.class,
            () -> accountService.createOrUpdateAccount(testAccount));

        assertEquals("User ID is required", exception.getMessage(), "Exception message should match");
        verify(userRepository, never()).findById(anyLong());
        verify(accountRepository, never()).save(any());
    }

    @Test
    void deleteAccount_ShouldCallRepositoryDelete() {
        doNothing().when(accountRepository).deleteById(1L);

        accountService.deleteAccount(1L);

        verify(accountRepository, times(1)).deleteById(1L);
    }
}