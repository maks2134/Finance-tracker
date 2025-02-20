package com.myfinance.financetracker.dao;

import com.myfinance.financetracker.model.Transaction;
import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с транзакциями.
 */
public interface TransactionDao {

    /**
   * Поиск транзакции по идентификатору.
   *
   * @param id идентификатор транзакции
   * @return Optional с найденной транзакцией или пустой, если транзакция не найдена
   */
    Optional<Transaction> findById(Long id);

    /**
     * Поиск всех транзакций.
     *
     * @return список транзакций
     */
    List<Transaction> findAll();

    /**
     * Сохранение транзакции.
     *
     * @param transaction транзакция для сохранения
     * @return сохраненная транзакция
     */
    Transaction save(Transaction transaction);

    void deleteById(Long id);

}