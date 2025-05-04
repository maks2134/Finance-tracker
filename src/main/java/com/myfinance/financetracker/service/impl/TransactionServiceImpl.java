// src/main/java/com/myfinance/financetracker/service/impl/TransactionServiceImpl.java
package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.exception.ResourceNotFoundException; // Добавь импорт
import com.myfinance.financetracker.model.Budget;
import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.repository.BudgetRepository;
import com.myfinance.financetracker.repository.TransactionRepository;
import com.myfinance.financetracker.service.TransactionService;
import jakarta.transaction.Transactional; // Добавь импорт для транзакционности
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  BudgetRepository budgetRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(String startDate, String endDate) {
         // ... (остается как было, но возможно стоит использовать Query) ...
        List<Transaction> allTransactions = transactionRepository.findAll(); // Неэффективно для больших данных

         return allTransactions.stream()
             .filter(tx -> {
                 String date = tx.getDate();
                 boolean afterStart = startDate == null || date == null || date.compareTo(startDate) >= 0; // Добавил проверку на null
                 boolean beforeEnd = endDate == null || date == null || date.compareTo(endDate) <= 0; // Добавил проверку на null
                 return afterStart && beforeEnd;
             })
             .toList();
    }

    @Override
    @Transactional // Важно! Обеспечивает выполнение всех операций в одной транзакции БД
    public Transaction createOrUpdateTransaction(Transaction transaction) {
        // Определяем, новая ли это транзакция (для обновления бюджета только при создании)
         boolean isNew = (transaction.getId() == null);
         Long budgetId = (transaction.getBudget() != null) ? transaction.getBudget().getId() : null;

         // Если есть budgetId, загружаем бюджет ПЕРЕД сохранением транзакции,
         // чтобы убедиться, что он существует и получить полный объект.
         Budget budgetToAssociate = null;
         if (budgetId != null) {
             budgetToAssociate = budgetRepository.findById(budgetId)
                 .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id " + budgetId + " when creating transaction"));
             // Привязываем ПОЛНЫЙ объект бюджета к транзакции перед сохранением
             transaction.setBudget(budgetToAssociate);
         } else {
             transaction.setBudget(null); // Убедимся, что бюджет null, если ID не пришел
         }

         // Теперь сохраняем транзакцию (она уже связана с полным объектом Budget, если он был)
         Transaction savedTransaction = transactionRepository.save(transaction);

         // Обновляем поле spent у ЗАГРУЖЕННОГО бюджета, если транзакция новая и бюджет был
         if (isNew && budgetToAssociate != null) {
             budgetToAssociate.setSpent(budgetToAssociate.getSpent() + savedTransaction.getAmount());
             // Сохраняем обновленный ПОЛНЫЙ объект бюджета (валидация должна пройти)
             budgetRepository.save(budgetToAssociate);
         }

         return savedTransaction; // Возвращаем сохраненную транзакцию
    }

    @Override
    @Transactional // Также лучше сделать транзакционным
    public void deleteTransaction(Long id) {
        // Перед удалением транзакции, возможно, нужно скорректировать spent у бюджета
        Optional<Transaction> transactionOpt = transactionRepository.findById(id);
        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            if (transaction.getBudget() != null) {
                Budget budget = transaction.getBudget();
                // Уменьшаем потраченную сумму (т.к. транзакция удаляется)
                budget.setSpent(budget.getSpent() - transaction.getAmount());
                // Проверяем, чтобы spent не стал отрицательным (на всякий случай)
                if (budget.getSpent() < 0) {
                   budget.setSpent(0.0);
                }
                budgetRepository.save(budget); // Сохраняем обновленный бюджет
            }
            transactionRepository.deleteById(id); // Удаляем саму транзакцию
        } else {
             throw new ResourceNotFoundException("Transaction not found with id " + id + " for deletion");
        }
    }


    @Override
    public List<Transaction> getTransactionsByUserAndDateRange(Long userId, String startDate, String endDate) {
        // Проверка на null для дат, т.к. Query может не сработать с null параметрами
        if (startDate == null || endDate == null) {
            // Можно вернуть пустой список или вызвать другой метод репозитория без дат
             // Например, найти все транзакции пользователя:
             // return transactionRepository.findByUserId(userId);
             // Пока вернем пустой для соответствия сигнатуре
             return List.of();
        }
        return transactionRepository.findTransactionsByUserAndDateRange(userId, startDate, endDate);
    }
}