package com.myfinance.financetracker.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryCache<K, V> {

    private final Map<K, V> cache = new ConcurrentHashMap<>();

    /**
     * Получить значение по ключу.
     *
     * @param key ключ
     * @return значение, если оно есть в кэше, иначе null
     */
    public V get(K key) {
        return cache.get(key);
    }

    /**
     * Добавить значение в кэш.
     *
     * @param key   ключ
     * @param value значение
     */
    public void put(K key, V value) {
        cache.put(key, value);
    }

    /**
     * Удалить значение из кэша по ключу.
     *
     * @param key ключ
     */
    public void evict(K key) {
        cache.remove(key);
    }

    /**
     * Очистить кэш.
     */
    public void clear() {
        cache.clear();
    }
}