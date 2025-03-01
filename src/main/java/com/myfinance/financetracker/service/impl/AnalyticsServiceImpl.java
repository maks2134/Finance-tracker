package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.AnalyticsRepository;
import com.myfinance.financetracker.service.AnalyticsService;
import com.myfinance.financetracker.service.SubscriptionService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final SubscriptionService subscriptionService;

    @Autowired
    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository,
                                SubscriptionService subscriptionService) {
        this.analyticsRepository = analyticsRepository;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public Optional<Analytics> getAnalyticsById(Long id, User user) {
        checkSubscription(user); // Проверяем подписку
        return analyticsRepository.findByIdAndUser(id, user);
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
        return analyticsRepository.save(analytics);
    }

    @Override
    public void deleteAnalytics(Long id, User user) {
        checkSubscription(user); // Проверяем подписку
        analyticsRepository.deleteByIdAndUser(id, user);
    }

    private void checkSubscription(User user) {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptionsByUser(user);
        boolean hasActiveSubscription = subscriptions.stream()
            .anyMatch(sub -> "ACTIVE".equals(sub.getStatus())
                && sub.getEndDate().isAfter(LocalDateTime.now()));

        if (!hasActiveSubscription) {
            throw new ResourceNotFoundException("Analytics is available"
                +
                " only for users with an active subscription.");
        }
    }
}