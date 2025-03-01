package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Subscription;
import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    Optional<Subscription> getSubscriptionById(Long id);

    List<Subscription> getAllSubscriptions();

    Subscription createOrUpdateSubscription(Subscription subscription);

    void deleteSubscription(Long id);
}