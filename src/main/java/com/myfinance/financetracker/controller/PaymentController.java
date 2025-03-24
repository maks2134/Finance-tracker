package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.exception.ValidationException;
import com.myfinance.financetracker.model.Payment;
import com.myfinance.financetracker.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment Controller", description = "API для управления платежами")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить платеж по ID", description = "Возвращает платеж по указанному ID")
    @ApiResponse(responseCode = "200", description = "Платеж найден")
    @ApiResponse(responseCode = "404", description = "Платеж не найден")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
        return ResponseEntity.ok(payment);
    }

    @GetMapping
    @Operation(summary = "Получить все платежи", description = "Возвращает список всех платежей")
    @ApiResponse(responseCode = "200", description = "Список платежей успешно получен")
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    @Operation(summary = "Создать платеж", description = "Создает новый платеж")
    @ApiResponse(responseCode = "200", description = "Платеж успешно создан")
    @ApiResponse(responseCode = "400", description = "Некорректные данные")
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        if (payment.getUser() == null || payment.getUser().getId() == null) {
            throw new ValidationException("User ID is required");
        }
        Payment createdPayment = paymentService.createOrUpdatePayment(payment);
        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить платеж", description = "Обновляет существующий платеж по ID")
    @ApiResponse(responseCode = "200", description = "Платеж успешно обновлен")
    @ApiResponse(responseCode = "404", description = "Платеж не найден")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @Valid @RequestBody Payment paymentDetails) {
        Payment payment = paymentService.getPaymentById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
        payment.setAmount(paymentDetails.getAmount());
        payment.setPaymentDate(paymentDetails.getPaymentDate());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        payment.setStatus(paymentDetails.getStatus());
        if (paymentDetails.getUser() != null && paymentDetails.getUser().getId() != null) {
            payment.setUser(paymentDetails.getUser());
        }
        Payment updatedPayment = paymentService.createOrUpdatePayment(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить платеж", description = "Удаляет платеж по ID")
    @ApiResponse(responseCode = "204", description = "Платеж успешно удален")
    @ApiResponse(responseCode = "404", description = "Платеж не найден")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}