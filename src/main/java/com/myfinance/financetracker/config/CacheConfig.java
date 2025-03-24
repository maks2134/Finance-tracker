package com.myfinance.financetracker.config;

import com.myfinance.financetracker.utils.InMemoryCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

    @Bean
    public InMemoryCache<Long, Object> accountCache() {
        return new InMemoryCache<>();
    }

    @Bean
    public InMemoryCache<Long, Object> analyticsCache() {
        return new InMemoryCache<>();
    }

    @Bean
    public InMemoryCache<Long, Object> transactionCache() {
        return new InMemoryCache<>();
    }
}