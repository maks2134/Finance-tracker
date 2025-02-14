package com.myfinance.financetracker.dao;

import com.myfinance.financetracker.model.Account;
import java.util.List;
import java.util.Optional;

public interface AccountDao {
  Optional<Account> findById(Long id);
  List<Account> findAll();
  Account save(Account account);
}