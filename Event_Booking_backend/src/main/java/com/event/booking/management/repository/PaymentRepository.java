package com.event.booking.management.repository;

import com.event.booking.management.model.Payment;
import com.event.booking.management.model.enums.PaymentStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    boolean existsByBookingIdAndPaymentStatus(Integer bookingId, PaymentStatus status);

    // Useful for locking / concurrency handling (see below)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Payment> findTopByBookingIdOrderByPaymentDateDesc(Integer bookingId);

    List<Payment> findByBookingId(Integer bookingId);
}
