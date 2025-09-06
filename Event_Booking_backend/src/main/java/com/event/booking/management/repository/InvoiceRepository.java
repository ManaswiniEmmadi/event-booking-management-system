package com.event.booking.management.repository;

import com.event.booking.management.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    Optional<Invoice> findByPayment_PaymentId(Integer paymentId);
}
