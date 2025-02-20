package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Account;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Optional<Account> getAccountById(Long id);

    List<Account> getAllAccounts();

    Account createOrUpdateAccount(Account account);

    void deleteAccount(Long id);
}