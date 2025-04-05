package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.model.enums.SubscriptionStatus;
import com.myfinance.financetracker.model.enums.SubscriptionType;
import com.myfinance.financetracker.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private Subscription subscription1;
    private Subscription subscription2;
    private User testUser;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("Test");

        subscription1 = new Subscription();
        subscription1.setId(1L);
        subscription1.setSubscriptionType(SubscriptionType.MONTHLY);
        subscription1.setStartDate(now);
        subscription1.setEndDate(now.plusMonths(1));
        subscription1.setStatus(SubscriptionStatus.ACTIVE);
        subscription1.setUser(testUser);

        subscription2 = new Subscription();
        subscription2.setId(2L);
        subscription2.setSubscriptionType(SubscriptionType.YEARLY);
        subscription2.setStartDate(now);
        subscription2.setEndDate(now.plusYears(1));
        subscription2.setStatus(SubscriptionStatus.ACTIVE);
        subscription2.setUser(testUser);
    }

    @Test
    void getSubscriptionById_ExistingId_ReturnsSubscription() {
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription1));

        Optional<Subscription> result = subscriptionService.getSubscriptionById(1L);

        assertTrue(result.isPresent());
        assertEquals(subscription1, result.get());
        assertEquals(SubscriptionType.MONTHLY, result.get().getSubscriptionType());
        assertEquals(SubscriptionStatus.ACTIVE, result.get().getStatus());
        verify(subscriptionRepository, times(1)).findById(1L);
    }

    @Test
    void getSubscriptionById_NonExistingId_ReturnsEmpty() {
        when(subscriptionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Subscription> result = subscriptionService.getSubscriptionById(99L);

        assertFalse(result.isPresent());
        verify(subscriptionRepository, times(1)).findById(99L);
    }

    @Test
    void getAllSubscriptions_ReturnsAllSubscriptions() {
        List<Subscription> allSubscriptions = Arrays.asList(subscription1, subscription2);
        when(subscriptionRepository.findAll()).thenReturn(allSubscriptions);

        List<Subscription> result = subscriptionService.getAllSubscriptions();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(allSubscriptions));
        assertEquals(SubscriptionType.YEARLY, result.get(1).getSubscriptionType());
        verify(subscriptionRepository, times(1)).findAll();
    }

    @Test
    void getAllSubscriptionsByUser_ReturnsUserSubscriptions() {
        List<Subscription> userSubscriptions = Arrays.asList(subscription1, subscription2);
        when(subscriptionRepository.findByUser(testUser)).thenReturn(userSubscriptions);

        List<Subscription> result = subscriptionService.getAllSubscriptionsByUser(testUser);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(userSubscriptions));
        assertEquals(testUser, result.get(0).getUser());
        verify(subscriptionRepository, times(1)).findByUser(testUser);
    }

    @Test
    void getAllSubscriptionsByUser_NoSubscriptions_ReturnsEmptyList() {
        when(subscriptionRepository.findByUser(testUser)).thenReturn(List.of());

        List<Subscription> result = subscriptionService.getAllSubscriptionsByUser(testUser);

        assertTrue(result.isEmpty());
        verify(subscriptionRepository, times(1)).findByUser(testUser);
    }

    @Test
    void createOrUpdateSubscription_NewSubscription_ReturnsSavedSubscription() {
        Subscription newSubscription = new Subscription();
        newSubscription.setSubscriptionType(SubscriptionType.MONTHLY);
        newSubscription.setStartDate(now);
        newSubscription.setEndDate(now.plusMonths(1));
        newSubscription.setStatus(SubscriptionStatus.ACTIVE);
        newSubscription.setUser(testUser);

        Subscription savedSubscription = new Subscription();
        savedSubscription.setId(3L);
        savedSubscription.setSubscriptionType(SubscriptionType.MONTHLY);
        savedSubscription.setStartDate(now);
        savedSubscription.setEndDate(now.plusMonths(1));
        savedSubscription.setStatus(SubscriptionStatus.ACTIVE);
        savedSubscription.setUser(testUser);

        when(subscriptionRepository.save(newSubscription)).thenReturn(savedSubscription);

        Subscription result = subscriptionService.createOrUpdateSubscription(newSubscription);

        assertEquals(savedSubscription, result);
        assertEquals(SubscriptionStatus.ACTIVE, result.getStatus());
        verify(subscriptionRepository, times(1)).save(newSubscription);
    }

    @Test
    void createOrUpdateSubscription_ExistingSubscription_ReturnsUpdatedSubscription() {
        subscription1.setStatus(SubscriptionStatus.CANCELLED);
        subscription1.setEndDate(now.plusDays(5));

        when(subscriptionRepository.save(subscription1)).thenReturn(subscription1);

        Subscription result = subscriptionService.createOrUpdateSubscription(subscription1);

        assertEquals(subscription1, result);
        assertEquals(SubscriptionStatus.CANCELLED, result.getStatus());
        verify(subscriptionRepository, times(1)).save(subscription1);
    }

    @Test
    void deleteSubscription_ValidId_DeletesSubscription() {
        doNothing().when(subscriptionRepository).deleteById(1L);

        subscriptionService.deleteSubscription(1L);

        verify(subscriptionRepository, times(1)).deleteById(1L);
    }

    @Test
    void createOrUpdateSubscription_ExpiredSubscription_SavesCorrectly() {
        Subscription expiredSubscription = new Subscription();
        expiredSubscription.setId(4L);
        expiredSubscription.setSubscriptionType(SubscriptionType.MONTHLY);
        expiredSubscription.setStartDate(now.minusMonths(2));
        expiredSubscription.setEndDate(now.minusMonths(1));
        expiredSubscription.setStatus(SubscriptionStatus.EXPIRED);
        expiredSubscription.setUser(testUser);

        when(subscriptionRepository.save(expiredSubscription)).thenReturn(expiredSubscription);

        Subscription result = subscriptionService.createOrUpdateSubscription(expiredSubscription);

        assertEquals(expiredSubscription, result);
        assertEquals(SubscriptionStatus.EXPIRED, result.getStatus());
        verify(subscriptionRepository, times(1)).save(expiredSubscription);
    }
}