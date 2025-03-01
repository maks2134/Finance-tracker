package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.SubscriptionRepository;
import com.myfinance.financetracker.service.SubscriptionService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id);
    }

    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public List<Subscription> getAllSubscriptionsByUser(User user) {
        return subscriptionRepository.findByUser(user); // Новый метод
    }

    @Override
    public Subscription createOrUpdateSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }
}