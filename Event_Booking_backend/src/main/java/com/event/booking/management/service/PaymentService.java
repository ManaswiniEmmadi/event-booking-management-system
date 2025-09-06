package com.event.booking.management.service;

import com.event.booking.management.DTO.PaymentRequestDTO;
import com.event.booking.management.DTO.PaymentResponseDTO;
import com.event.booking.management.exception.BookingNotFoundException;
import com.event.booking.management.exception.DuplicatePaymentException;
import com.event.booking.management.exception.InvalidPaymentException;
import com.event.booking.management.model.Booking;
import com.event.booking.management.model.Payment;
import com.event.booking.management.model.enums.PaymentStatus;
import com.event.booking.management.repository.BookingRepository;
import com.event.booking.management.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;

    public PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO) {

        // 1️ Validate Booking exists
        Booking booking = bookingRepository.findById(requestDTO.getBookingId().longValue())
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with ID: " + requestDTO.getBookingId()));

        // 2️ Check if payment already exists for this booking
        if (paymentRepository.existsByBookingIdAndPaymentStatus(requestDTO.getBookingId(), PaymentStatus.Success)) {
            throw new DuplicatePaymentException("A successful payment already exists for booking ID: " + requestDTO.getBookingId());
        }

        // 3️ Validate amount
        if (requestDTO.getAmount() == null || requestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Payment amount must be greater than zero");
        }

        // 4️ Generate unique transaction ID
        String generatedTxnId = "TXN" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();

        // 5️ Simulate payment status
        PaymentStatus status;
        Random rand = new Random();
        int chance = rand.nextInt(100);

        if (chance < 95) {
            status = PaymentStatus.Success;
            booking.setStatus("confirmed"); // Mark booking as confirmed
            bookingRepository.save(booking);
        } else {
            status = PaymentStatus.Failed;
        }

        // 6️ Save payment
        Payment payment = Payment.builder()
                .bookingId(requestDTO.getBookingId())
                .userId(requestDTO.getUserId())
                .paymentMethod(requestDTO.getPaymentMethod())
                .transactionId(generatedTxnId)
                .paymentStatus(status)
                .amount(requestDTO.getAmount())
                .build();

        Payment saved = paymentRepository.save(payment);

        // 7️ Return Response
        return PaymentResponseDTO.builder()
                .paymentId(saved.getPaymentId())
                .bookingId(saved.getBookingId())
                .userId(saved.getUserId())
                .paymentMethod(saved.getPaymentMethod())
                .transactionId(saved.getTransactionId())
                .paymentStatus(saved.getPaymentStatus())
                .amount(saved.getAmount())
                .paymentDate(saved.getPaymentDate())
                .build();
    }
    // For internal use
    public Payment getPaymentEntityById(Integer paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new InvalidPaymentException("Payment not found with ID: " + paymentId));
    }

    // For API
    public PaymentResponseDTO getPaymentById(Integer paymentId) {
        Payment payment = getPaymentEntityById(paymentId);

        return PaymentResponseDTO.builder()
                .paymentId(payment.getPaymentId())
                .bookingId(payment.getBookingId())
                .userId(payment.getUserId())
                .paymentMethod(payment.getPaymentMethod())
                .transactionId(payment.getTransactionId())
                .paymentStatus(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .build();
    }

    public List<Payment> getPaymentsByBookingId(Integer bookingId) {
        return paymentRepository.findByBookingId(bookingId);
    }



}
