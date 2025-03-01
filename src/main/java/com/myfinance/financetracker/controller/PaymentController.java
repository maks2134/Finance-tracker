package com.myfinance.financetracker.controller;

import com.myfinance.financetracker.exception.ResourceNotFoundException;
import com.myfinance.financetracker.model.Payment;
import com.myfinance.financetracker.service.PaymentService;
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
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getPaymentById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
        return ResponseEntity.ok(payment);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment createdPayment = paymentService.createOrUpdatePayment(payment);
        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id,
                                                 @RequestBody Payment paymentDetails) {
        Payment payment = paymentService.getPaymentById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + id));
        payment.setAmount(paymentDetails.getAmount());
        payment.setPaymentDate(paymentDetails.getPaymentDate());
        payment.setPaymentMethod(paymentDetails.getPaymentMethod());
        payment.setStatus(paymentDetails.getStatus());
        Payment updatedPayment = paymentService.createOrUpdatePayment(payment);
        return ResponseEntity.ok(updatedPayment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}