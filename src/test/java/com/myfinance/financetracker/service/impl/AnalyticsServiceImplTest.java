package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.model.enums.SubscriptionStatus;
import com.myfinance.financetracker.repository.AnalyticsRepository;
import com.myfinance.financetracker.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    private Analytics analytics1;
    private Analytics analytics2;
    private User testUser;
    private Subscription activeSubscription;
    private Subscription expiredSubscription;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("Test");

        analytics1 = new Analytics();
        analytics1.setId(1L);
        analytics1.setAnalysisDate(now.minusDays(1));
        analytics1.setAnalysisResult("Spending analysis");
        analytics1.setRecommendations("Reduce entertainment expenses");
        analytics1.setUser(testUser);

        analytics2 = new Analytics();
        analytics2.setId(2L);
        analytics2.setAnalysisDate(now);
        analytics2.setAnalysisResult("Income analysis");
        analytics2.setRecommendations("Increase savings rate");
        analytics2.setUser(testUser);

        activeSubscription = new Subscription();
        activeSubscription.setId(1L);
        activeSubscription.setStatus(SubscriptionStatus.ACTIVE);
        activeSubscription.setEndDate(now.plusDays(30));
        activeSubscription.setUser(testUser);

        expiredSubscription = new Subscription();
        expiredSubscription.setId(2L);
        expiredSubscription.setStatus(SubscriptionStatus.EXPIRED);
        expiredSubscription.setEndDate(now.minusDays(1));
        expiredSubscription.setUser(testUser);
    }

    @Test
    void getAnalyticsById_WithActiveSubscription_ReturnsAnalytics() {
        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(activeSubscription));
        when(analyticsRepository.findByIdAndUser(1L, testUser))
            .thenReturn(Optional.of(analytics1));

        Optional<Analytics> result = analyticsService.getAnalyticsById(1L, testUser);

        assertTrue(result.isPresent());
        assertEquals(analytics1, result.get());
        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, times(1)).findByIdAndUser(1L, testUser);
    }

    @Test
    void getAnalyticsById_WithoutActiveSubscription_ThrowsException() {
        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(expiredSubscription));

        assertThrows(ResourceNotFoundException.class, () -> {
            analyticsService.getAnalyticsById(1L, testUser);
        });

        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, never()).findByIdAndUser(anyLong(), any());
    }

    @Test
    void getAllAnalytics_WithActiveSubscription_ReturnsAllAnalytics() {
        List<Analytics> allAnalytics = Arrays.asList(analytics1, analytics2);
        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(activeSubscription));
        when(analyticsRepository.findByUser(testUser)).thenReturn(allAnalytics);

        List<Analytics> result = analyticsService.getAllAnalytics(testUser);

        assertEquals(2, result.size());
        assertTrue(result.containsAll(allAnalytics));
        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, times(1)).findByUser(testUser);
    }

    @Test
    void getAllAnalytics_WithoutActiveSubscription_ThrowsException() {
        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(expiredSubscription));

        assertThrows(ResourceNotFoundException.class, () -> {
            analyticsService.getAllAnalytics(testUser);
        });

        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, never()).findByUser(any());
    }

    @Test
    void createOrUpdateAnalytics_WithActiveSubscription_ReturnsSavedAnalytics() {
        Analytics newAnalytics = new Analytics();
        newAnalytics.setAnalysisDate(now);
        newAnalytics.setAnalysisResult("New analysis");
        newAnalytics.setRecommendations("New recommendations");

        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(activeSubscription));
        when(analyticsRepository.save(newAnalytics)).thenReturn(newAnalytics);

        Analytics result = analyticsService.createOrUpdateAnalytics(newAnalytics, testUser);

        assertEquals(newAnalytics, result);
        assertEquals(testUser, result.getUser());
        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, times(1)).save(newAnalytics);
    }

    @Test
    void createOrUpdateAnalytics_WithoutActiveSubscription_ThrowsException() {
        Analytics newAnalytics = new Analytics();
        newAnalytics.setAnalysisDate(now);
        newAnalytics.setAnalysisResult("New analysis");
        newAnalytics.setRecommendations("New recommendations");

        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(expiredSubscription));

        assertThrows(ResourceNotFoundException.class, () -> {
            analyticsService.createOrUpdateAnalytics(newAnalytics, testUser);
        });

        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, never()).save(any());
    }

    @Test
    void deleteAnalytics_WithActiveSubscription_DeletesAnalytics() {
        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(activeSubscription));
        doNothing().when(analyticsRepository).deleteByIdAndUser(1L, testUser);

        analyticsService.deleteAnalytics(1L, testUser);

        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, times(1)).deleteByIdAndUser(1L, testUser);
    }

    @Test
    void deleteAnalytics_WithoutActiveSubscription_ThrowsException() {
        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Collections.singletonList(expiredSubscription));

        assertThrows(ResourceNotFoundException.class, () -> {
            analyticsService.deleteAnalytics(1L, testUser);
        });

        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
        verify(analyticsRepository, never()).deleteByIdAndUser(anyLong(), any());
    }

    @Test
    void checkSubscription_MultipleSubscriptions_ChecksCorrectly() {
        Subscription anotherActiveSubscription = new Subscription();
        anotherActiveSubscription.setId(3L);
        anotherActiveSubscription.setStatus(SubscriptionStatus.ACTIVE);
        anotherActiveSubscription.setEndDate(now.plusDays(15));

        when(subscriptionService.getAllSubscriptionsByUser(testUser))
            .thenReturn(Arrays.asList(activeSubscription, expiredSubscription, anotherActiveSubscription));
        when(analyticsRepository.findByIdAndUser(1L, testUser))
            .thenReturn(Optional.of(analytics1));

        Optional<Analytics> result = analyticsService.getAnalyticsById(1L, testUser);

        assertTrue(result.isPresent());
        verify(subscriptionService, times(1)).getAllSubscriptionsByUser(testUser);
    }
}