package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.utils.InMemoryCache;
import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Account;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.AccountRepository;
import com.myfinance.financetracker.repository.UserRepository;
import com.myfinance.financetracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final InMemoryCache<Long, Account> accountCache; // Кэш для аккаунтов

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserRepository userRepository,
                              InMemoryCache<Long, Account> accountCache) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountCache = accountCache;
    }

    @Override
    public Optional<Account> getAccountById(Long id) {
        // Проверяем кэш
        Account cachedAccount = accountCache.get(id);
        if (cachedAccount != null) {
            return Optional.of(cachedAccount);
        }

        // Если в кэше нет, запрашиваем из БД
        Optional<Account> account = accountRepository.findById(id);
        account.ifPresent(acc -> accountCache.put(id, acc)); // Сохраняем в кэш
        return account;
    }

    @Override
    public List<Account> getAllAccounts() {
        // Для списка кэширование не применяем, так как данные могут часто меняться
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

        // Сохраняем в БД
        Account savedAccount = accountRepository.save(account);

        // Обновляем кэш
        accountCache.put(savedAccount.getId(), savedAccount);
        return savedAccount;
    }

    @Override
    public void deleteAccount(Long id) {
        // Удаляем из БД
        accountRepository.deleteById(id);

        // Удаляем из кэша
        accountCache.evict(id);
    }
}