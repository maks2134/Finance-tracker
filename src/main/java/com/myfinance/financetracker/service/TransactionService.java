package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Transaction;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для бизнес-логики, связанной с транзакциями.
 */
public interface TransactionService {

    /**
     * Получение транзакции по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return Optional с транзакцией, если найдена
     */
    Optional<Transaction> getTransactionById(Long id);

    /**
     * Получение транзакций в указанном диапазоне дат.
     *
     * @param startDate начало диапазона (в формате yyyy-MM-dd)
     * @param endDate конец диапазона (в формате yyyy-MM-dd)
     * @return список транзакций, удовлетворяющих критериям
     */
    List<Transaction> getTransactionsByDateRange(String startDate, String endDate);

    /**
     * Сохранение (или обновление) транзакции.
     *
     * @param transaction транзакция для сохранения
     * @return сохранённая транзакция
     */
    Transaction createOrUpdateTransaction(Transaction transaction);
}