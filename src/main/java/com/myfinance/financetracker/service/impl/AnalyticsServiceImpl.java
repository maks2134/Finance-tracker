package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.model.enums.SubscriptionStatus;
import com.myfinance.financetracker.utils.InMemoryCache;
import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.AnalyticsRepository;
import com.myfinance.financetracker.service.AnalyticsService;
import com.myfinance.financetracker.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final SubscriptionService subscriptionService;
    private final InMemoryCache<Long, Analytics> analyticsCache; // Кэш для аналитики

    @Autowired
    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository,
                                SubscriptionService subscriptionService,
                                InMemoryCache<Long, Analytics> analyticsCache) {
        this.analyticsRepository = analyticsRepository;
        this.subscriptionService = subscriptionService;
        this.analyticsCache = analyticsCache;
    }

    @Override
    public Optional<Analytics> getAnalyticsById(Long id, User user) {
        checkSubscription(user); // Проверяем подписку

        // Проверяем кэш
        Analytics cachedAnalytics = analyticsCache.get(id);
        if (cachedAnalytics != null) {
            return Optional.of(cachedAnalytics);
        }

        // Если в кэше нет, запрашиваем из БД
        Optional<Analytics> analytics = analyticsRepository.findByIdAndUser(id, user);
        analytics.ifPresent(anal -> analyticsCache.put(id, anal)); // Сохраняем в кэш
        return analytics;
    }

    @Override
    public List<Analytics> getAllAnalytics(User user) {
        checkSubscription(user); // Проверяем подписку
        return analyticsRepository.findByUser(user);
    }

    @Override
    public Analytics createOrUpdateAnalytics(Analytics analytics, User user) {
        checkSubscription(user); // Проверяем подписку
        analytics.setUser(user);

        // Сохраняем в БД
        Analytics savedAnalytics = analyticsRepository.save(analytics);

        // Обновляем кэш
        analyticsCache.put(savedAnalytics.getId(), savedAnalytics);
        return savedAnalytics;
    }

    @Override
    public void deleteAnalytics(Long id, User user) {
        checkSubscription(user); // Проверяем подписку

        // Удаляем из БД
        analyticsRepository.deleteByIdAndUser(id, user);

        // Удаляем из кэша
        analyticsCache.evict(id);
    }

    private void checkSubscription(User user) {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsByUser(user);
        boolean hasActiveSubscription = subscriptions.stream()
            .anyMatch(sub -> sub.getStatus() == SubscriptionStatus.ACTIVE // Сравнение через == для enum
                && sub.getEndDate().isAfter(LocalDateTime.now()));

        if (!hasActiveSubscription) {
            throw new ResourceNotFoundException("Аналитика доступна только для пользователей с активной подпиской.");
        }
    }
}