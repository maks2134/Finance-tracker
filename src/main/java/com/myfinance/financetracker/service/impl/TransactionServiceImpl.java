package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.repository.TransactionRepository;
import com.myfinance.financetracker.service.TransactionService;
import com.myfinance.financetracker.utils.InMemoryCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final InMemoryCache<Long, Transaction> singleTransactionCache;
    private final InMemoryCache<String, List<Transaction>> transactionListCache;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  BudgetRepository budgetRepository,
                                  @Qualifier("singleTransactionCache") InMemoryCache<Long, Transaction> singleTransactionCache,
                                  @Qualifier("transactionListCache") InMemoryCache<String, List<Transaction>> transactionListCache) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.singleTransactionCache = singleTransactionCache;
        this.transactionListCache = transactionListCache;
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        Transaction cachedTransaction = singleTransactionCache.get(id);
        if (cachedTransaction != null) {
            return Optional.of(cachedTransaction);
        }

        Optional<Transaction> transaction = transactionRepository.findById(id);
        transaction.ifPresent(t -> singleTransactionCache.put(t.getId(), t));
        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        String cacheKey = "transactions_" + (startDate != null ? startDate : "null") +
            "_" + (endDate != null ? endDate : "null");

        List<Transaction> cachedTransactions = transactionListCache.get(cacheKey);
        if (cachedTransactions != null) {
            return cachedTransactions;
        }

        List<Transaction> allTransactions = transactionRepository.findAll();

        List<Transaction> filteredTransactions = allTransactions.stream()
            .filter(tx -> {
                String date = tx.getDate();
                boolean afterStart = startDate == null || date.compareTo(startDate) >= 0;
                boolean beforeEnd = endDate == null || date.compareTo(endDate) <= 0;
                return afterStart && beforeEnd;
            })
            .toList(); // Changed from collect(Collectors.toList())

        transactionListCache.put(cacheKey, filteredTransactions);
        return filteredTransactions;
    }

    @Override
    public Transaction createOrUpdateTransaction(Transaction transaction) {
        boolean isNew = (transaction.getId() == null);
        Transaction savedTransaction = transactionRepository.save(transaction);

        if (isNew && transaction.getBudget() != null) {
            Budget budget = transaction.getBudget();
            budget.setSpent(budget.getSpent() + transaction.getAmount());
            budgetRepository.save(budget);
        }

        singleTransactionCache.put(savedTransaction.getId(), savedTransaction);
        transactionListCache.clear();

        return savedTransaction;
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
        singleTransactionCache.evict(id);
        transactionListCache.clear();
    }

    @Override
    public List<Transaction> getTransactionsByUserAndDateRange(Long userId, String startDate, String endDate) {
        String cacheKey = "user_" + userId + "_" +
            (startDate != null ? startDate : "null") +
            "_" + (endDate != null ? endDate : "null");

        List<Transaction> cachedTransactions = transactionListCache.get(cacheKey);
        if (cachedTransactions != null) {
            return cachedTransactions;
        }

        List<Transaction> transactions = transactionRepository.findTransactionsByUserAndDateRange(userId, startDate, endDate);
        transactionListCache.put(cacheKey, transactions);
        return transactions;
    }
}