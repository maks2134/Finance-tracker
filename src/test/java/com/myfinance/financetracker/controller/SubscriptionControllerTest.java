package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Test
    void getSubscriptionById_ShouldReturnSubscription() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);
        when(subscriptionService.getSubscriptionById(1L)).thenReturn(Optional.of(subscription));

        ResponseEntity<Subscription> response = subscriptionController.getSubscriptionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getSubscriptionById_ShouldThrowNotFound() {
        when(subscriptionService.getSubscriptionById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionController.getSubscriptionById(1L);
        });
    }

    @Test
    void getAllSubscriptions_ShouldReturnAllSubscriptions() {
        List<Subscription> subscriptions = Arrays.asList(new Subscription(), new Subscription());
        when(subscriptionService.getAllSubscriptions()).thenReturn(subscriptions);

        ResponseEntity<List<Subscription>> response = subscriptionController.getAllSubscriptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createSubscription_ShouldReturnCreatedSubscription() {
        Subscription subscription = new Subscription();
        when(subscriptionService.createOrUpdateSubscription(subscription)).thenReturn(subscription);

        ResponseEntity<Subscription> response = subscriptionController.createSubscription(subscription);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteSubscription_ShouldReturnNoContent() {
        doNothing().when(subscriptionService).deleteSubscription(1L);

        ResponseEntity<Void> response = subscriptionController.deleteSubscription(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}