package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.service.SubscriptionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id "
                + id));
        return ResponseEntity.ok(subscription);
    }

    @GetMapping
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription
                                                                   subscription) {
        Subscription createdSubscription = subscriptionService.createOrUpdateSubscription(
            subscription);
        return ResponseEntity.ok(createdSubscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id,
                                                           @RequestBody
                                                           Subscription subscriptionDetails) {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id "
                + id));
        subscription.setSubscriptionType(subscriptionDetails.getSubscriptionType());
        subscription.setStartDate(subscriptionDetails.getStartDate());
        subscription.setEndDate(subscriptionDetails.getEndDate());
        subscription.setStatus(subscriptionDetails.getStatus());
        Subscription updatedSubscription = subscriptionService.createOrUpdateSubscription(
            subscription);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}