package com.myfinance.financetracker.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("squid:S6829")
@Component
public class InMemoryCache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(InMemoryCache.class);

    private static class CacheEntry<V> {
        private final V value;
        private final long expiryTime;

        public CacheEntry(V value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        public V getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= expiryTime;
        }
    }

    private final Map<K, CacheEntry<V>> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public InMemoryCache() {
        this(300_000); // Значение по умолчанию: 5 минут
    }

    public InMemoryCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
        scheduler.scheduleAtFixedRate(this::evictExpiredEntries, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
    }

    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            logger.info("Cache miss for key: {}", key);
            return null;
        }
        if (entry.isExpired()) {
            logger.info("Cache entry expired for key: {}", key);
            cache.remove(key);
            return null;
        }
        logger.info("Cache hit for key: {}", key);
        return entry.getValue();
    }

    public void put(K key, V value) {
        CacheEntry<V> entry = new CacheEntry<>(value, System.currentTimeMillis() + ttlMillis);
        cache.put(key, entry);
        logger.info("Cache put for key: {}", key);
    }

    public void evict(K key) {
        cache.remove(key);
        logger.info("Cache evict for key: {}", key);
    }

    public void clear() {
        cache.clear();
        logger.info("Cache cleared");
    }

    private void evictExpiredEntries() {
        for (Map.Entry<K, CacheEntry<V>> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                cache.remove(entry.getKey());
                logger.info("Cache entry evicted for key: {}", entry.getKey());
            }
        }
    }
}