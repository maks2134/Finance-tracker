package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.repository.TransactionRepository;
import com.myfinance.financetracker.service.TransactionService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
        List<Transaction> allTransactions = transactionRepository.findAll();

        if (startDate == null && endDate == null) {
            return allTransactions;
        }

        return allTransactions.stream()
            .filter(tx -> {
                String date = tx.getDate();
                boolean afterStart = startDate == null || date.compareTo(startDate) >= 0;
                boolean beforeEnd = endDate == null || date.compareTo(endDate) <= 0;
                return afterStart && beforeEnd;
            })
            .toList();
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
        return savedTransaction;
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}