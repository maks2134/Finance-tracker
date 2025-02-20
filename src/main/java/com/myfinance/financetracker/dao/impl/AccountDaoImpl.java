package com.myfinance.financetracker.dao.impl;

import com.myfinance.financetracker.dao.AccountDao;
import com.myfinance.financetracker.model.Account;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;



@Repository
public class AccountDaoImpl implements AccountDao {

    private final ConcurrentHashMap<Long, Account> accountStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public AccountDaoImpl() {
        // Предварительная инициализация тестовыми данными
        Account acc1 = new Account(idGenerator.getAndIncrement(),
            "Основной счет", "Банковский", new BigDecimal("5000.0"));
        Account acc2 = new Account(idGenerator.getAndIncrement(),
            "Сберегательный счет", "Банковский", new BigDecimal("10000.0"));
        accountStore.put(acc1.getId(), acc1);
        accountStore.put(acc2.getId(), acc2);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accountStore.get(id));
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accountStore.values());
    }

    @Override
    public Account save(Account account) {
        if (account.getId() == null) {
            account.setId(idGenerator.getAndIncrement());
        }
        accountStore.put(account.getId(), account);
        return account;
    }

    @Override
    public void deleteById(Long id) {
        accountStore.remove(id); // Новый метод
    }
}