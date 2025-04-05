package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.AnalyticsService;
import com.myfinance.financetracker.service.UserService;
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
class AnalyticsControllerTest {

    @Mock
    private AnalyticsService analyticsService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AnalyticsController analyticsController;

    @Test
    void getAnalyticsById_ShouldReturnAnalytics() {
        User user = new User();
        user.setId(1L);
        Analytics analytics = new Analytics();
        analytics.setId(1L);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(analyticsService.getAnalyticsById(1L, user)).thenReturn(Optional.of(analytics));

        ResponseEntity<Analytics> response = analyticsController.getAnalyticsById(1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getAllAnalytics_ShouldReturnAllAnalytics() {
        User user = new User();
        user.setId(1L);
        List<Analytics> analyticsList = Arrays.asList(new Analytics(), new Analytics());

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(analyticsService.getAllAnalytics(user)).thenReturn(analyticsList);

        ResponseEntity<List<Analytics>> response = analyticsController.getAllAnalytics(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createAnalytics_ShouldReturnCreatedAnalytics() {
        User user = new User();
        user.setId(1L);
        Analytics analytics = new Analytics();

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(analyticsService.createOrUpdateAnalytics(analytics, user)).thenReturn(analytics);

        ResponseEntity<Analytics> response = analyticsController.createAnalytics(analytics, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateAnalytics_ShouldReturnUpdatedAnalytics() {
        User user = new User();
        user.setId(1L);
        Analytics existing = new Analytics();
        existing.setId(1L);
        Analytics updates = new Analytics();
        updates.setAnalysisResult("Updated");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        when(analyticsService.getAnalyticsById(1L, user)).thenReturn(Optional.of(existing));
        when(analyticsService.createOrUpdateAnalytics(any(), any())).thenReturn(existing);

        ResponseEntity<Analytics> response = analyticsController.updateAnalytics(1L, updates, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteAnalytics_ShouldReturnNoContent() {
        User user = new User();
        user.setId(1L);

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));
        doNothing().when(analyticsService).deleteAnalytics(1L, user);

        ResponseEntity<Void> response = analyticsController.deleteAnalytics(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}