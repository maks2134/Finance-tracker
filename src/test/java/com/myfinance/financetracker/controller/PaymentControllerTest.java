package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.exception.ValidationException;
import com.myfinance.financetracker.model.Payment;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.service.PaymentService;
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
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void getPaymentById_ShouldReturnPayment() {
        Payment payment = new Payment();
        payment.setId(1L);
        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(payment));

        ResponseEntity<Payment> response = paymentController.getPaymentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getPaymentById_ShouldThrowNotFound() {
        when(paymentService.getPaymentById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            paymentController.getPaymentById(1L);
        });
    }

    @Test
    void getAllPayments_ShouldReturnAllPayments() {
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());
        when(paymentService.getAllPayments()).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void createPayment_ShouldReturnCreatedPayment() {
        Payment payment = new Payment();
        User user = new User();
        user.setId(1L);
        payment.setUser(user);
        when(paymentService.createOrUpdatePayment(payment)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.createPayment(payment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createPayment_ShouldThrowValidationException() {
        Payment payment = new Payment();
        payment.setUser(new User()); // User without ID

        assertThrows(ValidationException.class, () -> {
            paymentController.createPayment(payment);
        });
    }

    @Test
    void updatePayment_ShouldReturnUpdatedPayment() {
        Payment existingPayment = new Payment();
        existingPayment.setId(1L);
        Payment updatedDetails = new Payment();
        updatedDetails.setAmount(100.0);

        when(paymentService.getPaymentById(1L)).thenReturn(Optional.of(existingPayment));
        when(paymentService.createOrUpdatePayment(any())).thenReturn(existingPayment);

        ResponseEntity<Payment> response = paymentController.updatePayment(1L, updatedDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody().getAmount());
    }

    @Test
    void deletePayment_ShouldReturnNoContent() {
        doNothing().when(paymentService).deletePayment(1L);

        ResponseEntity<Void> response = paymentController.deletePayment(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}