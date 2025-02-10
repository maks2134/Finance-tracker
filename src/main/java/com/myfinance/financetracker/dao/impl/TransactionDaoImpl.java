package com.myfinance.financetracker.dao.impl;

import com.myfinance.financetracker.dao.TransactionDao;
import com.myfinance.financetracker.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Реализация TransactionDao с использованием in-memory хранилища.
 */
@Repository
public class TransactionDaoImpl implements TransactionDao {

  private final ConcurrentHashMap<Long, Transaction> transactionStore = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  /**
   * Конструктор с предварительной инициализацией тестовыми данными.
   */
  public TransactionDaoImpl() {
    Transaction tx1 = new Transaction(idGenerator.getAndIncrement(), 150.0, "2025-01-15", "Оплата коммунальных услуг");
    Transaction tx2 = new Transaction(idGenerator.getAndIncrement(), 2000.0, "2025-01-20", "Зарплата");
    transactionStore.put(tx1.getId(), tx1);
    transactionStore.put(tx2.getId(), tx2);
  }

  @Override
  public Optional<Transaction> findById(final Long id) {
    return Optional.ofNullable(transactionStore.get(id));
  }

  @Override
  public List<Transaction> findAll() {
    return new ArrayList<>(transactionStore.values());
  }

  @Override
  public Transaction save(final Transaction transaction) {
    if (transaction.getId() == null) {
      transaction.setId(idGenerator.getAndIncrement());
    }
    transactionStore.put(transaction.getId(), transaction);
    return transaction;
  }
}