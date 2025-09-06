package com.event.booking.management.model;

import com.event.booking.management.model.enums.PaymentMethod;
import com.event.booking.management.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    private Integer bookingId;

    private Integer userId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String transactionId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private BigDecimal amount;

    private LocalDateTime paymentDate = LocalDateTime.now();

    @PrePersist
    public void setPaymentDateIfAbsent() {
        this.paymentDate = LocalDateTime.now();
    }
}
