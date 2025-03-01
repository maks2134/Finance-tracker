package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.User;
import java.util.List;
import java.util.Optional;

public interface AnalyticsService {
    Optional<Analytics> getAnalyticsById(Long id, User user);

    List<Analytics> getAllAnalytics(User user);

    Analytics createOrUpdateAnalytics(Analytics analytics, User user);

    void deleteAnalytics(Long id, User user);
}