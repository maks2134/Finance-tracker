package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.dao.AccountDao;
import com.myfinance.financetracker.model.Account;
import com.myfinance.financetracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

  private final AccountDao accountDao;

  @Autowired
  public AccountServiceImpl(AccountDao accountDao) {
    this.accountDao = accountDao;
  }

  @Override
  public Optional<Account> getAccountById(Long id) {
    return accountDao.findById(id);
  }

  @Override
  public List<Account> getAllAccounts() {
    return accountDao.findAll();
  }

  @Override
  public Account createOrUpdateAccount(Account account) {
    return accountDao.save(account);
  }
}