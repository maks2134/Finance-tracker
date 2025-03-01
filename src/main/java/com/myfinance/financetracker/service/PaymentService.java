package com.myfinance.financetracker.service;

import com.myfinance.financetracker.model.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Optional<Payment> getPaymentById(Long id);

    List<Payment> getAllPayments();

    Payment createOrUpdatePayment(Payment payment);

    void deletePayment(Long id);
}