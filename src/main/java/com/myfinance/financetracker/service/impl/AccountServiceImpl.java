package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Account;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.AccountRepository;
import com.myfinance.financetracker.repository.UserRepository;
import com.myfinance.financetracker.service.AccountService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account createOrUpdateAccount(Account account) {
        // Проверяем, что пользователь существует
        if (account.getUser() == null || account.getUser().getId() == null) {
            throw new ResourceNotFoundException("User ID is required");
        }
        User user = userRepository.findById(account.getUser().getId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id "
                + account.getUser().getId()));
        account.setUser(user);
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}