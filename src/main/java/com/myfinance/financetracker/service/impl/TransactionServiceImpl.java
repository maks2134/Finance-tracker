package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.dao.BudgetDao;
import com.myfinance.financetracker.dao.TransactionDao;
import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.service.TransactionService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final BudgetDao budgetDao;

    @Autowired
    public TransactionServiceImpl(final TransactionDao transactionDao, final BudgetDao budgetDao) {
        this.transactionDao = transactionDao;
        this.budgetDao = budgetDao;
    }

    @Override
    public Optional<Transaction> getTransactionById(final Long id) {
        return transactionDao.findById(id);
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(final String startDate, final String endDate) {
        List<Transaction> allTransactions = transactionDao.findAll();

        // Если параметры не заданы, возвращаем все транзакции
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
    public Transaction createOrUpdateTransaction(final Transaction transaction) {
        // Определяем, создаётся ли новая транзакция
        boolean isNew = (transaction.getId() == null);
        Transaction savedTransaction = transactionDao.save(transaction);
        if (isNew && transaction.getBudget() != null) {
            Budget budget = transaction.getBudget();
            // Обновляем сумму потраченного бюджета
            budget.setSpent(budget.getSpent() + transaction.getAmount());
            budgetDao.save(budget);
        }
        return savedTransaction;
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionDao.deleteById(id);
    }
}