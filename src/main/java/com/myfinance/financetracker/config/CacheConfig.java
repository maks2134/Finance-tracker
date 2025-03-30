package com.myfinance.financetracker.config;

import com.myfinance.financetracker.model.Transaction;
import com.myfinance.financetracker.utils.InMemoryCache;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public InMemoryCache<Long, Object> accountCache() {
        return new InMemoryCache<>(300_000, 200);
    }

    @Bean("singleTransactionCache")
    public InMemoryCache<Long, Transaction> singleTransactionCache() {
        return new InMemoryCache<>(300_000, 100);
    }

    @Bean("transactionListCache")
    public InMemoryCache<String, List<Transaction>> transactionListCache() {
        return new InMemoryCache<>(300_000, 100);
    }
}