package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.AnalyticsService;
import com.myfinance.financetracker.service.UserService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private static final String USER_NOT_FOUND = "User not found with id";
    private final AnalyticsService analyticsService;
    private final UserService userService;

    @Autowired
    public AnalyticsController(AnalyticsService analyticsService, UserService userService) {
        this.analyticsService = analyticsService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Analytics> getAnalyticsById(@PathVariable Long id,
                                                      @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        Analytics analytics = analyticsService.getAnalyticsById(id, user)
            .orElseThrow(() -> new ResourceNotFoundException("Analytics not found" + id));
        return ResponseEntity.ok(analytics);
    }

    @GetMapping
    public ResponseEntity<List<Analytics>> getAllAnalytics(@RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        List<Analytics> analytics = analyticsService.getAllAnalytics(user);
        return ResponseEntity.ok(analytics);
    }

    @PostMapping
    public ResponseEntity<Analytics> createAnalytics(@RequestBody Analytics analytics,
                                                     @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        Analytics createdAnalytics = analyticsService.createOrUpdateAnalytics(analytics, user);
        return ResponseEntity.ok(createdAnalytics);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Analytics> updateAnalytics(@PathVariable Long id,
                                                     @RequestBody Analytics analyticsDetails,
                                                     @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        Analytics analytics = analyticsService.getAnalyticsById(id, user)
            .orElseThrow(() -> new ResourceNotFoundException("Analytics not found with id " + id));
        analytics.setAnalysisDate(analyticsDetails.getAnalysisDate());
        analytics.setAnalysisResult(analyticsDetails.getAnalysisResult());
        analytics.setRecommendations(analyticsDetails.getRecommendations());
        Analytics updatedAnalytics = analyticsService.createOrUpdateAnalytics(analytics, user);
        return ResponseEntity.ok(updatedAnalytics);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalytics(@PathVariable Long id, @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        analyticsService.deleteAnalytics(id, user);
        return ResponseEntity.noContent().build();
    }
}