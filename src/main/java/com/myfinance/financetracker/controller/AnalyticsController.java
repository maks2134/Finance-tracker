package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Analytics;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.AnalyticsService;
import com.myfinance.financetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Analytics Controller", description = "API для управления аналитикой")
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
    @Operation(summary = "Получить аналитику по ID", description = "Возвращает аналитику по ID для указанного пользователя")
    @ApiResponse(responseCode = "200", description = "Аналитика найдена")
    @ApiResponse(responseCode = "404", description = "Аналитика или пользователь не найдены")
    public ResponseEntity<Analytics> getAnalyticsById(
        @Parameter(description = "ID аналитики", required = true) @PathVariable Long id,
        @Parameter(description = "ID пользователя", required = true) @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        Analytics analytics = analyticsService.getAnalyticsById(id, user)
            .orElseThrow(() -> new ResourceNotFoundException("Analytics not found" + id));
        return ResponseEntity.ok(analytics);
    }

    @GetMapping
    @Operation(summary = "Получить всю аналитику", description = "Возвращает всю аналитику для указанного пользователя")
    @ApiResponse(responseCode = "200", description = "Аналитика успешно получена")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<List<Analytics>> getAllAnalytics(
        @Parameter(description = "ID пользователя", required = true) @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        List<Analytics> analytics = analyticsService.getAllAnalytics(user);
        return ResponseEntity.ok(analytics);
    }

    @PostMapping
    @Operation(summary = "Создать аналитику", description = "Создает новую аналитику для указанного пользователя")
    @ApiResponse(responseCode = "200", description = "Аналитика успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    public ResponseEntity<Analytics> createAnalytics(
        @Valid @RequestBody Analytics analytics,
        @Parameter(description = "ID пользователя", required = true) @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        Analytics createdAnalytics = analyticsService.createOrUpdateAnalytics(analytics, user);
        return ResponseEntity.ok(createdAnalytics);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить аналитику", description = "Обновляет аналитику по ID для указанного пользователя")
    @ApiResponse(responseCode = "200", description = "Аналитика успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Аналитика или пользователь не найдены")
    public ResponseEntity<Analytics> updateAnalytics(
        @Parameter(description = "ID аналитики", required = true) @PathVariable Long id,
        @Valid @RequestBody Analytics analyticsDetails,
        @Parameter(description = "ID пользователя", required = true) @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        Analytics analytics = analyticsService.getAnalyticsById(id, user)
            .orElseThrow(() -> new ResourceNotFoundException("Analytics not found with id " + id));
        analytics.setAnalysisDate(analyticsDetails.getAnalysisDate());
        analytics.setAnalysisResult(analyticsDetails.getAnalysisResult());
        analytics.setRecommendations(analyticsDetails.getRecommendations());
        Analytics updatedAnalytics = analyticsService.createOrUpdateAnalytics(analytics, user);
        return ResponseEntity.ok(updatedAnalytics);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить аналитику", description = "Удаляет аналитику по ID для указанного пользователя")
    @ApiResponse(responseCode = "204", description = "Аналитика успешно удалена")
    @ApiResponse(responseCode = "404", description = "Аналитика или пользователь не найдены")
    public ResponseEntity<Void> deleteAnalytics(
        @Parameter(description = "ID аналитики", required = true) @PathVariable Long id,
        @Parameter(description = "ID пользователя", required = true) @RequestParam Long userId) {
        User user = userService.getUserById(userId)
            .orElseThrow(() -> new ResourceNotFoundException(USER_NOT_FOUND + userId));
        analyticsService.deleteAnalytics(id, user);
        return ResponseEntity.noContent().build();
    }
}