package com.myfinance.financetracker.service.impl;

import com.myfinance.financetracker.model.Payment;
import com.myfinance.financetracker.model.User;
import com.myfinance.financetracker.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment1;
    private Payment payment2;
    private User testUser;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("Test");

        payment1 = new Payment();
        payment1.setId(1L);
        payment1.setAmount(100.0);
        payment1.setPaymentDate(now);
        payment1.setPaymentMethod("Credit Card");
        payment1.setStatus("SUCCESS");
        payment1.setUser(testUser);

        payment2 = new Payment();
        payment2.setId(2L);
        payment2.setAmount(50.0);
        payment2.setPaymentDate(now.minusDays(1));
        payment2.setPaymentMethod("PayPal");
        payment2.setStatus("PENDING");
        payment2.setUser(testUser);
    }

    @Test
    void getPaymentById_ExistingId_ReturnsPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment1));

        Optional<Payment> result = paymentService.getPaymentById(1L);

        assertTrue(result.isPresent());
        assertEquals(payment1, result.get());
        assertEquals("Credit Card", result.get().getPaymentMethod());
        assertEquals("SUCCESS", result.get().getStatus());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void getPaymentById_NonExistingId_ReturnsEmpty() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Payment> result = paymentService.getPaymentById(99L);

        assertFalse(result.isPresent());
        verify(paymentRepository, times(1)).findById(99L);
    }

    @Test
    void getAllPayments_ReturnsAllPayments() {
        List<Payment> allPayments = Arrays.asList(payment1, payment2);
        when(paymentRepository.findAll()).thenReturn(allPayments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(allPayments));
        assertEquals("PayPal", result.get(1).getPaymentMethod());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void createOrUpdatePayment_NewPayment_ReturnsSavedPayment() {
        Payment newPayment = new Payment();
        newPayment.setAmount(75.0);
        newPayment.setPaymentDate(now.plusDays(1));
        newPayment.setPaymentMethod("Bank Transfer");
        newPayment.setStatus("PENDING");
        newPayment.setUser(testUser);

        Payment savedPayment = new Payment();
        savedPayment.setId(3L);
        savedPayment.setAmount(75.0);
        savedPayment.setPaymentDate(now.plusDays(1));
        savedPayment.setPaymentMethod("Bank Transfer");
        savedPayment.setStatus("PENDING");
        savedPayment.setUser(testUser);

        when(paymentRepository.save(newPayment)).thenReturn(savedPayment);

        Payment result = paymentService.createOrUpdatePayment(newPayment);

        assertEquals(savedPayment, result);
        assertEquals("PENDING", result.getStatus());
        verify(paymentRepository, times(1)).save(newPayment);
    }

    @Test
    void createOrUpdatePayment_ExistingPayment_ReturnsUpdatedPayment() {
        payment1.setStatus("FAILED");
        payment1.setAmount(90.0);

        when(paymentRepository.save(payment1)).thenReturn(payment1);

        Payment result = paymentService.createOrUpdatePayment(payment1);

        assertEquals(payment1, result);
        assertEquals("FAILED", result.getStatus());
        assertEquals(90.0, result.getAmount());
        verify(paymentRepository, times(1)).save(payment1);
    }

    @Test
    void deletePayment_ValidId_DeletesPayment() {
        doNothing().when(paymentRepository).deleteById(1L);

        paymentService.deletePayment(1L);

        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void createOrUpdatePayment_ZeroAmount_SavesCorrectly() {
        Payment zeroAmountPayment = new Payment();
        zeroAmountPayment.setId(4L);
        zeroAmountPayment.setAmount(0.0);
        zeroAmountPayment.setPaymentDate(now);
        zeroAmountPayment.setPaymentMethod("Free");
        zeroAmountPayment.setStatus("SUCCESS");
        zeroAmountPayment.setUser(testUser);

        when(paymentRepository.save(zeroAmountPayment)).thenReturn(zeroAmountPayment);

        Payment result = paymentService.createOrUpdatePayment(zeroAmountPayment);

        assertEquals(zeroAmountPayment, result);
        assertEquals(0.0, result.getAmount());
        verify(paymentRepository, times(1)).save(zeroAmountPayment);
    }

    @Test
    void createOrUpdatePayment_VerifiesUserAssociation() {
        Payment newPayment = new Payment();
        newPayment.setAmount(200.0);
        newPayment.setPaymentDate(now);
        newPayment.setPaymentMethod("Crypto");
        newPayment.setStatus("SUCCESS");
        newPayment.setUser(testUser);

        Payment savedPayment = new Payment();
        savedPayment.setId(5L);
        savedPayment.setAmount(200.0);
        savedPayment.setPaymentDate(now);
        savedPayment.setPaymentMethod("Crypto");
        savedPayment.setStatus("SUCCESS");
        savedPayment.setUser(testUser);

        when(paymentRepository.save(newPayment)).thenReturn(savedPayment);

        Payment result = paymentService.createOrUpdatePayment(newPayment);

        assertEquals(testUser, result.getUser());
        verify(paymentRepository, times(1)).save(newPayment);
    }
}