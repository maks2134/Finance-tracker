package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Payment;
import com.myfinance.financetracker.repository.PaymentRepository;
import com.myfinance.financetracker.service.PaymentService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment createOrUpdatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}