package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Subscription;
import com.myfinance.financetracker.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
@Tag(name = "Subscription Controller", description = "API для управления подписками")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить подписку по ID", description = "Возвращает подписку по указанному ID")
    @ApiResponse(responseCode = "200", description = "Подписка найдена")
    @ApiResponse(responseCode = "404", description = "Подписка не найдена")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        return ResponseEntity.ok(subscription);
    }

    @GetMapping
    @Operation(summary = "Получить все подписки", description = "Возвращает список всех подписок")
    @ApiResponse(responseCode = "200", description = "Список подписок успешно получен")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping
    @Operation(summary = "Создать подписку", description = "Создает новую подписку")
    @ApiResponse(responseCode = "200", description = "Подписка успешно создана")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public ResponseEntity<Subscription> createSubscription(@Valid @RequestBody Subscription subscription) {
        Subscription createdSubscription = subscriptionService.createOrUpdateSubscription(subscription);
        return ResponseEntity.ok(createdSubscription);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить подписку", description = "Обновляет существующую подписку по ID")
    @ApiResponse(responseCode = "200", description = "Подписка успешно обновлена")
    @ApiResponse(responseCode = "404", description = "Подписка не найдена")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, @Valid @RequestBody Subscription subscriptionDetails) {
        Subscription subscription = subscriptionService.getSubscriptionById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Subscription not found with id " + id));
        subscription.setSubscriptionType(subscriptionDetails.getSubscriptionType());
        subscription.setStartDate(subscriptionDetails.getStartDate());
        subscription.setEndDate(subscriptionDetails.getEndDate());
        subscription.setStatus(subscriptionDetails.getStatus());
        Subscription updatedSubscription = subscriptionService.createOrUpdateSubscription(subscription);
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить подписку", description = "Удаляет подписку по ID")
    @ApiResponse(responseCode = "204", description = "Подписка успешно удалена")
    @ApiResponse(responseCode = "404", description = "Подписка не найдена")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.noContent().build();
    }
}